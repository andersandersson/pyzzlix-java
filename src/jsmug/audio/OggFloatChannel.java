package jsmug.audio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.BufferUtils;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

public class OggFloatChannel implements PCMFloatChannel {
	// Objects for reading ogg
	private Packet joggPacket = new Packet();
	private Page joggPage = new Page();
	private StreamState joggStreamState = new StreamState();
	private SyncState joggSyncState = new SyncState();

	// Objects for decoding vorbis
	private DspState jorbisDspState = new DspState();
	private Block jorbisBlock = new Block(jorbisDspState);
	private Comment jorbisComment = new Comment();
	private Info jorbisInfo = new Info();

	private FileChannel input;
	private int bufferSize = 2048;
	private ByteBuffer buffer = null;
	private FloatBuffer seekBuffer = null;

    private float[][][] pcmInfo = null;
    private int[] pcmIndex = null;
    
	private int channels = 0;
	private int sampleRate = 0;
	
	private long sampleCount = 0;
	private long size = 0;
	
	private boolean endOfStream = false;
	private boolean needNewPacket = true;
	private boolean hasPacket = false;
	
	private boolean initialized = false;
	private boolean isOpen = false;
	
	private void log(String string) {
		System.out.println(string);
	}
	
	
	public OggFloatChannel(FileChannel input) {
		this.input = input;
		this.isOpen = input.isOpen();
	}
	
	
	private boolean init() {
		this.joggSyncState.init();
		
		if(!this.readHeader()) {
			return false;
		}
		
		if(!this.initSound()) {
			return false;
		}

		this.initialized = true;
		this.sampleCount = 0;
		this.seekBuffer = BufferUtils.createFloatBuffer(4096);

		return true;
	}
	
	
	// Read Vorbis headers from stream
	private boolean readHeader() {
		boolean pageRead;
		boolean packetRead;
		
		pageRead = this.readNextPage(true);
		if(!pageRead) {
			log("Error reading header page 1");
			return false;
		}
		
		packetRead = this.readNextPacket();
		if(!packetRead) {
			log("Error reading header page 1, packet 1");
			return false;
		}

		// Initialize Info and Comment objects
		this.jorbisInfo.init();
		this.jorbisComment.init();
		
		// Extract Info and Comment info from the packet
		if(this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) == -1) {
			log("Could not interpret the first packet");
			return false;
		}

		// Read next 2 pages and extract comment info
		for(int i=0; i<2; i++) {
	 		pageRead = this.readNextPage();
			if(!pageRead) {
				log("Error reading header page "+(i+2));
				return false;
			}

			packetRead = this.readNextPacket();
			if(!packetRead) {
				log("Error reading header page "+(i+2)+", packet 1");
				return false;
			}

			// Parse the jorbis comment and info
			if(this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) == -1) {
				log("Could not interpret packet 1 in header page "+(i+2));
				return false;
			}
		}

		return true;
	}
	

	// Initialize for OGG Vorbis sound
	private boolean initSound() {
		// Initialize DSP synthesis
		this.jorbisDspState.synthesis_init(this.jorbisInfo);
		
		// Make Block object aware of the DSP
		this.jorbisBlock.init(this.jorbisDspState);
		
		// Get sound info
		this.channels = this.jorbisInfo.channels;
		this.sampleRate = this.jorbisInfo.rate;
        
        // Create the PCM objects 
        this.pcmInfo = new float[1][][];
        this.pcmIndex = new int[jorbisInfo.channels];

        return true;
	}

	
	// Read a new page of data
	private void readData() {
		int index = 0;
		int count = 0;
		
		// Try to read bufferSize more data
		index = this.joggSyncState.buffer(this.bufferSize);
		
		if(index >= 0) {
			this.buffer = ByteBuffer.wrap(this.joggSyncState.data, index, this.bufferSize);
			try {
				count = (int) this.input.read(this.buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			count = 0;
		}

		// Tell syncstate what we have read
        this.joggSyncState.wrote(count);
		
        // If there is no more data, we have reached eof
        if(count == 0) {
			this.endOfStream = true;
        }
	}
	
	
	// Extract the next page from the stream
	private boolean readNextPage(boolean first) {
		boolean pageFetched = false;
		int pageResult = 0;
		
		while(!pageFetched) {
			// Keep reading data until pageout succeeds or we reach end of stream
			while(pageResult == 0 && !this.endOfStream) {
				pageResult = this.joggSyncState.pageout(this.joggPage);
				
				if(pageResult == 0) {
					this.readData();
				}
			}
			
			// If we need more data without reaching end of page above,
			// consider end of stream
			if(pageResult == 0) {
				this.endOfStream = true;
				return false;
			}

			// If the page fails, try next page
			if(pageResult == -1) {
				log("There is a hole in the page data. We proceed.");
				break;
			} else {
				// If it is the first time we read a page, reset streamstate
				if(first) {
					this.joggStreamState.init(this.joggPage.serialno());
					this.joggStreamState.reset();
				}
				
				// Read in the new page and return
				this.joggStreamState.pagein(this.joggPage);			
				pageFetched = true;
			}
		}
		
		return pageFetched;
	}
	
	
	// Default parameter for readNextPage
	private boolean readNextPage() {
		return this.readNextPage(false);
	}
	
	
	// Extract the next packet from the page (or read new page if needed)
	private boolean readNextPacket() {
		boolean packetFetched = false;
		
		// Loop until we found a packet or reached end of file
		while(!packetFetched && !this.endOfStream) {
			
			// Try to read next packet
			int packetResult = this.joggStreamState.packetout(this.joggPacket);

			// If we got 0, we need more data
			if(packetResult == 0) {
				this.readNextPage();
			} else if(packetResult == -1) {
				log("There is a hole in the packet data. We proceed.");
			} else {
				// No error, i.e. packet fetched
				packetFetched = true;
			}
		}
		
		return packetFetched;
	}
	
	
	// Decode the current packet to PCM float format
	private void decodeNextPacket(FloatBuffer dst, long maxCount) {	
		// Check that the packet is a audio data packet etc.
		if(this.needNewPacket) {
	        if(this.jorbisBlock.synthesis(this.joggPacket) == 0)
	        {
	            // Give the block to the DspState object.
	        	this.jorbisDspState.synthesis_blockin(this.jorbisBlock);
	        }

	        // Unless it is changed to true, no new packets should be fetched
			this.needNewPacket = false;
		}
        
		long samples=0; // How many samples jorbis has
        long range=0;   // How many we can handle
        
        boolean done = false;
        
        // Read samples from dspstate
        while(!done) {        	
        	// If we have reached the end of the packet
        	if((samples = this.jorbisDspState.synthesis_pcmout(this.pcmInfo, this.pcmIndex)) <= 0) {
        		done = true;
        		this.needNewPacket = true;
        		break;
        	}
        	
        	// If we don't have space even for 1 sample
        	if(dst.remaining() < this.channels) {
        		done = true;
        		break;
        	}
        	
        	// Only read as much as fits
        	if(samples*this.channels > dst.remaining()) {
        		range = dst.remaining()/this.channels;
        		done = true;
        	} else {
        		range = samples;
        	}
        	
        	// If we are trying to exceed maxCount, truncate and consider packet done
        	if(maxCount > -1 && (this.sampleCount+range) > maxCount) {
        		range = maxCount-this.sampleCount;
        		done = true;
        		this.needNewPacket = true;
        	}

        	// Read pcm samples
        	for(int i=0; i<range; i++) {
        		for(int j=0; j<this.channels; j++) {
        			dst.put(this.pcmInfo[0][j][this.pcmIndex[j] + i]);
        		}
        	}
        	
        	this.jorbisDspState.synthesis_read((int)range);
        	this.sampleCount += range;
        }        
	}

	private boolean assertChannel() {
		if(!this.isOpen) {
			return false;
		}
		
		if(!this.initialized) {
			this.init();
		}
		
		return true;
	}
	

	@Override
	public long position() {
		if(!this.assertChannel()) { return -1; }
		
		return this.sampleCount;
	}


	@Override
	public PCMFloatChannel position(long position) {
		if(!this.assertChannel()) { return null; }

		if(position < 0) {
			throw new IllegalArgumentException("Position must be positive, got: " + position);
		}
		
		this.endOfStream = false;
		this.hasPacket = false;
		this.needNewPacket = true;
		this.sampleCount = 0;

		boolean found = false; 

		try {
			this.input.position(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.joggSyncState.reset();
		this.joggSyncState.init();

		// Read first page with initialization
		this.readHeader();
		this.initSound();
		
		// Keep reading packets and pages until position is found
		while(!found) {
			if(this.needNewPacket) {
				if(!this.readNextPacket()) {
					// End of stream reached. do nothing
					return this;
				}
			}

			// Reset seek buffer and decode next packet
			this.seekBuffer.position(0);
			this.decodeNextPacket(this.seekBuffer, position);
					
			// If sampleCount equals position we have found the place
			if(this.sampleCount == position) {
				found = true;
			}
			
			if(this.endOfStream) {
				found = false;
				break;
			}
		}
		
		return this;
	}

	
	// Read next part from stream to dst
	public long read(FloatBuffer dst) {		
		if(!this.assertChannel()) { return -2; }
		
		return this.read(new FloatBuffer[]{dst}, 0, 1);
	}

	
	@Override
	public long read(FloatBuffer[] dsts) {
		if(!this.assertChannel()) { return -2; }
		
		return this.read(dsts, 0, dsts.length);
	}


	@Override
	public long read(FloatBuffer dst, long position) {
		if(!this.assertChannel()) { return -2; }
		
		// Save current position
		long oldPosition = this.sampleCount;
		
		// Move to given position
		this.position(position);
		
		// Read floats and save floats read
		long length = this.read(new FloatBuffer[]{dst}, 0, 1);
		
		// Reset position back to original
		this.position(oldPosition);
				
		return length;
	}


	@Override
	public long read(FloatBuffer[] dsts, int offset, int length) {
		if(!this.assertChannel()) { return -2; }
		
		// If we have reached end of stream, return -1
		if(this.endOfStream) {
			return -1;
		}
		
		// Check arguments 
		if(offset < 0) {
			throw new IllegalArgumentException("Offset must be non-negative, got: "+offset);
		}
		
		if(length < 0) {
			throw new IllegalArgumentException("Length must be non-negative, got: "+length);
		}
		
		// Read more data as long as endOfStream is not reached and
		// there is space left in dst
		int currentDst = offset;
		long startPos = this.sampleCount;
		
		while(!this.endOfStream && currentDst < offset+length) {
			FloatBuffer dst = dsts[currentDst];
			
			// If we have reached the end of a packet, read next
			if(this.needNewPacket) {
				this.hasPacket = this.readNextPacket();
			}
			
			// Decode the next packet into dst
			if(this.hasPacket) {
				this.decodeNextPacket(dst, (int) this.joggPage.granulepos());
			}
			
			// Go to next dst if this is full
			if(!dst.hasRemaining()) {
				currentDst++;
			}
		}

		return this.sampleCount - startPos;
	}


	@Override
	public void close() {
		this.isOpen = false;
		
		this.joggStreamState.clear();
		this.joggSyncState.clear();
		this.jorbisDspState.clear();
		this.jorbisBlock.clear();
		this.jorbisInfo.clear();

		if(this.input != null) {
			try {
				this.input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public boolean isOpen() {
		return this.isOpen;
	}


	@Override
	public long size() {
		if(!this.assertChannel()) { return -2; }
		
		// If we have calculated size before, use that value
		if(this.size > 0) {
			return this.size;
		}
		
		long oldPosition = this.sampleCount;
		
		// Set position at beginning of stream
		this.position(0);		
		while(this.readNextPage()) {
			// do nothing
		}
		
		// Last page granuleposition will be the size of the stream.
		this.size = this.joggPage.granulepos();
		
		// Return to old position
		this.position(oldPosition);
		
		return this.size;
	}
	
	
	@Override
	public int getChannels() {
		if(!this.initialized) {
			this.init();
		}

		return this.channels;
	}


	@Override
	public int getSampleRate() {
		if(!this.initialized) {
			this.init();
		}

		return this.sampleRate;
	}


	@Override
	public int getBits() {		
		if(!this.initialized) {
			this.init();
		}

		return 16;
	}
}

package jsmug.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.badlogic.gdx.Gdx;

public class Files {
	private static class InternalFileChannel extends FileChannel {
		File file = null;
		InputStream input = null;
		long position = 0;
		
		public InternalFileChannel(File file) {
			this.file = file;

			try {
				this.reset();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void force(boolean metaData) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public FileLock lock(long position, long size, boolean shared) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public long position() throws IOException {
			return this.position;
		}

		@Override
		public FileChannel position(long newPosition) throws IOException {
			if(newPosition < 0) {
				throw new IllegalArgumentException("Position must be positive");
			}
			
			if(this.position == newPosition) {
				return this;
			} if(newPosition < this.position) {
				this.reset();
				this.input.skip(newPosition);
			} else {
				this.input.skip(newPosition - this.position);
			}

			this.position = newPosition;
			
			return this;
		}

		@Override
		public int read(ByteBuffer dst) throws IOException {
			int count = this.input.read(dst.array(), dst.position(), dst.limit()-dst.position());
			
			this.position += count;
			
			return count;
		}

		@Override
		public int read(ByteBuffer dst, long position) throws IOException {
			long oldPosition = this.position;
			
			this.position(position);
			
			int count = this.read(dst);
			
			this.position(oldPosition);
			
			return count;
		}

		@Override
		public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
			long count = 0;
			
			for(int i=offset; i<length; i++) {
				if(i < dsts.length) {
					count += this.input.read(dsts[i].array(), dsts[i].position(), dsts[i].limit()-dsts[i].position());
				}
			}
			
			this.position += count;
			return count;
		}

		@Override
		public long size() throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public FileChannel truncate(long size) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public FileLock tryLock(long position, long size, boolean shared) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public int write(ByteBuffer src) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public int write(ByteBuffer src, long position) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}

		@Override
		protected void implCloseChannel() throws IOException {
			throw new UnsupportedOperationException("Method not implemented yet!");
		}
		
		protected void reset() throws FileNotFoundException {
			try {
				if(this.input != null) {
					this.input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(file.exists()) {
				try {
					this.input = new FileInputStream(this.file.getPath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				this.input = Files.class.getResourceAsStream("/" + file.getPath().replace('\\', '/'));
				
				if(this.input == null) {
					throw new FileNotFoundException("File not found: " + "/" + file.getPath().replace('\\', '/'));
				}
			}
		}
	}
	
	public static class FileHandle {
		public enum Mode { INTERNAL, EXTERNAL, CLASSPATH };
		
		private File file = null;
		private Mode mode = Mode.INTERNAL;
		
		public FileHandle(String path, Mode mode) {
			this.file = new File(path);
			this.mode = mode;
		}
		
		public FileChannel getReadChannel() throws FileNotFoundException {
			if(this.file.exists()) {
				FileInputStream input = new FileInputStream(this.file.getPath());
				return input.getChannel();
			} else {
				return new InternalFileChannel(this.file);
			}
		}
	}
	
	public static FileHandle internal(String path) {
		return new FileHandle(path, FileHandle.Mode.INTERNAL);
	}
}

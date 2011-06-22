package se.nederlag.pyzzlix;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import se.nederlag.pyzzlix.audio.MusicInputStreamMixer;
import se.nederlag.pyzzlix.audio.OggMusicInputStream;
import se.nederlag.pyzzlix.audio.OpenALMusicStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.openal.OpenALAudio;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tests.utils.GdxTest;

public class MusicTest extends GdxTest {

        @Override public boolean needsGL20 () {
                return false;
        }

        static final int NUM_STREAMS = 1; 
        Music[] music = new Music[NUM_STREAMS];
        TextureRegion buttons;
        SpriteBatch batch;
        BitmapFont font;  
        
        float x = 0.0f;
        
        @Override public void create() {
                for(int i = 0; i < music.length; i++) {
                		OggMusicInputStream input1 = new OggMusicInputStream(Gdx.files.internal("data/music1_bass.ogg"));
                		OggMusicInputStream input2 = new OggMusicInputStream(Gdx.files.internal("data/music1_bass2.ogg"));
                		OggMusicInputStream input3 = new OggMusicInputStream(Gdx.files.internal("data/music1_lead2.ogg"));
                		OggMusicInputStream input4 = new OggMusicInputStream(Gdx.files.internal("data/music1_lead3.ogg"));
                		OggMusicInputStream input5 = new OggMusicInputStream(Gdx.files.internal("data/music1_kick.ogg"));

                		MusicInputStreamMixer mixer = new MusicInputStreamMixer(5, input1.getChannels(), input1.getSampleRate());
                		mixer.setStream(0, input1);
                		mixer.setStream(1, input2);
                		mixer.setStream(2, input3);
                		mixer.setStream(3, input4);
                		mixer.setStream(4, input5);
                		music[i] = new OpenALMusicStream((OpenALAudio) Gdx.audio, mixer);
                		music[i].setLooping(true);

                        //music[i] = Gdx.audio.newMusic(Gdx.files.internal("data/music1_lead3.ogg"));
//                      music = Gdx.audio.newMusic(Gdx.files.internal("data/sell_buy_item.wav"));       
//                      music[i] = Gdx.audio.newMusic(Gdx.files.internal("data/threeofaperfectpair.mp3"));
                }
                //OpenALMusicStream music
            	OggMusicInputStream input = new OggMusicInputStream(Gdx.files.internal("data/music1_lead3.ogg"));
                buttons = new TextureRegion(new Texture(Gdx.files.internal("data/blocks.png")), 0, 0, 64, 64);
                batch = new SpriteBatch();
                font = new BitmapFont();
        }
        
        @Override public void resize(int width, int height) {
                batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        }
        
        @Override public void render() {
                Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
                batch.begin();
                batch.draw(buttons, 0, 0);
                //batch.draw(buttons, 64, 0);
                //batch.draw(buttons, 128, 0);
                batch.draw(buttons, x, 128);
                //font.draw(batch, "\"Three of a perfect pair: " +  music[0].getPosition(), 10, Gdx.graphics.getHeight() - 20);
                batch.end();                    
                
                if(Gdx.input.justTouched()) {
                        if(Gdx.input.getY() > Gdx.graphics.getHeight() - 64) {
                                if(Gdx.input.getX() < 64) {
                                        for(int i = 0; i < music.length; i++) music[i].play();
                                }
                                if(Gdx.input.getX() > 64 && Gdx.input.getX() < 128) {
                                        for(int i = 0; i < music.length; i++) music[i].stop();
                                }
                                if(Gdx.input.getX() > 128 && Gdx.input.getX() < 192) {
                                        for(int i = 0; i < music.length; i++) music[i].pause();
                                }
                        }
                }
                x += 0.5f;
        }
}


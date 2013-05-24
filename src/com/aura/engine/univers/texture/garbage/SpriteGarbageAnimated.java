package com.aura.engine.univers.texture.garbage;

import com.aura.base.Aura;
import com.aura.base.thread.AuraThread;
import com.aura.engine.univers.texture.EPSpriteCM;

public class SpriteGarbageAnimated<E> extends SpriteGarbage<E> {
	public SpriteGarbageAnimated(EPSpriteCM spriteManager) {
		super(spriteManager);
	}
	
	@Override
	public void setFocus(E e) {
		pause();
		super.setFocus(e);
	}
	
	private AuraThread playThread;
	public synchronized void play(Aura aura, int speedMs) {
		pause();
		if (playThread == null) {
			playThread = new AuraThread(aura, "PLAY_SPRITE", speedMs) {
				@Override public void doOnClose() {}
				@Override public boolean condition() { return true; }
				@Override
				public void action() {
					getFocus().next();
				}
			};
			playThread.start();
		} else {
			playThread.resumeThread(speedMs);
		}
	}
	
	public synchronized void pause() {
		if (playThread != null)
			playThread.pauseThread();
	}
	public synchronized void stop() {
		if (playThread != null)
			playThread.forceTerminate();
	}
}
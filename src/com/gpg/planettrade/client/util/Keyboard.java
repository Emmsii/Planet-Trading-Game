package com.gpg.planettrade.client.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard implements KeyListener{

	public class Key{
		public int presses, absorbs;
		public boolean down, pressed;
		
		public Key(){
			keys.add(this);
		}
		
		public void toggle(boolean pressed){
			if(pressed != down) down = pressed;
			if(pressed) presses++;
		}
		
		public void update(){
			if(absorbs < presses){
				absorbs++;
				pressed = true;
			}else pressed = false;
		}		
	}
	
	public List<Key> keys = new ArrayList<Key>();
	
	public Key a = new Key();
	public Key b = new Key();
	public Key c = new Key();
	public Key d = new Key();
	public Key e = new Key();
	public Key f = new Key();
	public Key g = new Key();
	public Key h = new Key();
	public Key i = new Key();
	public Key j = new Key();
	public Key k = new Key();
	public Key l = new Key();
	public Key m = new Key();
	public Key n = new Key();
	public Key o = new Key();
	public Key p = new Key();
	public Key q = new Key();
	public Key r = new Key();
	public Key s = new Key();
	public Key t = new Key();
	public Key u = new Key();
	public Key v = new Key();
	public Key w = new Key();
	public Key x = new Key();
	public Key y = new Key();
	public Key z = new Key();
	
	public Key zero = new Key();
	public Key one = new Key();
	public Key two = new Key();
	public Key three = new Key();
	public Key four = new Key();
	public Key five = new Key();
	public Key six = new Key();
	public Key seven = new Key();
	public Key eight = new Key();
	public Key nine = new Key();
	
	public Key shift = new Key();
	public Key space = new Key();
	public Key backSpace = new Key();
	public Key enter = new Key();
	
	public void update(){
		for(Key key : keys) key.update();
	}
	
	public void release(){
		for(int i = 0; i < keys.size(); i++){
			keys.get(i).pressed = false;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyCode == KeyEvent.VK_A) a.toggle(isPressed);
		if(keyCode == KeyEvent.VK_B) b.toggle(isPressed);
		if(keyCode == KeyEvent.VK_C) c.toggle(isPressed);
		if(keyCode == KeyEvent.VK_D) d.toggle(isPressed);
		if(keyCode == KeyEvent.VK_E) e.toggle(isPressed);
		if(keyCode == KeyEvent.VK_F) f.toggle(isPressed);
		if(keyCode == KeyEvent.VK_G) g.toggle(isPressed);
		if(keyCode == KeyEvent.VK_H) h.toggle(isPressed);
		if(keyCode == KeyEvent.VK_I) i.toggle(isPressed);
		if(keyCode == KeyEvent.VK_J) j.toggle(isPressed);
		if(keyCode == KeyEvent.VK_K) k.toggle(isPressed);
		if(keyCode == KeyEvent.VK_L) l.toggle(isPressed);
		if(keyCode == KeyEvent.VK_M) m.toggle(isPressed);
		if(keyCode == KeyEvent.VK_N) n.toggle(isPressed);
		if(keyCode == KeyEvent.VK_O) o.toggle(isPressed);
		if(keyCode == KeyEvent.VK_P) p.toggle(isPressed);
		if(keyCode == KeyEvent.VK_Q) q.toggle(isPressed);
		if(keyCode == KeyEvent.VK_R) r.toggle(isPressed);
		if(keyCode == KeyEvent.VK_S) s.toggle(isPressed);
		if(keyCode == KeyEvent.VK_T) t.toggle(isPressed);
		if(keyCode == KeyEvent.VK_U) u.toggle(isPressed);
		if(keyCode == KeyEvent.VK_V) v.toggle(isPressed);
		if(keyCode == KeyEvent.VK_W) w.toggle(isPressed);
		if(keyCode == KeyEvent.VK_X) x.toggle(isPressed);
		if(keyCode == KeyEvent.VK_Y) y.toggle(isPressed);
		if(keyCode == KeyEvent.VK_Z) z.toggle(isPressed);
		
		if(keyCode == KeyEvent.VK_0 || keyCode == KeyEvent.VK_NUMPAD0) zero.toggle(isPressed);
		if(keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1) one.toggle(isPressed);
		if(keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2) two.toggle(isPressed);
		if(keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3) three.toggle(isPressed);
		if(keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4) four.toggle(isPressed);
		if(keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_NUMPAD5) five.toggle(isPressed);
		if(keyCode == KeyEvent.VK_6 || keyCode == KeyEvent.VK_NUMPAD6) six.toggle(isPressed);
		if(keyCode == KeyEvent.VK_7 || keyCode == KeyEvent.VK_NUMPAD7) seven.toggle(isPressed);
		if(keyCode == KeyEvent.VK_8 || keyCode == KeyEvent.VK_NUMPAD8) eight.toggle(isPressed);
		if(keyCode == KeyEvent.VK_9 || keyCode == KeyEvent.VK_NUMPAD9) nine.toggle(isPressed);
		
		if(keyCode == KeyEvent.VK_SHIFT) shift.toggle(isPressed);
		if(keyCode == KeyEvent.VK_SPACE) space.toggle(isPressed);
		if(keyCode == KeyEvent.VK_BACK_SPACE) backSpace.toggle(isPressed);
		if(keyCode == KeyEvent.VK_ENTER) enter.toggle(isPressed);
		
		//TODO: Do stuff like . , ! ? ( )
//		if(keyCode == KeyEvent.VK_SHIFT) if(keyCode == KeyEvent.VK_BACK_SLASH) questionmark.togggle(isPressed);
	}
	
}

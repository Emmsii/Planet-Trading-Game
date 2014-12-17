package com.gpg.planettrade.client.component;

import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Text;

public class TextField extends Component{

	protected String text = "";
	protected int maxLength;
	private Keyboard key;
	
	public TextField(int x, int y, int maxLength, Keyboard key){
		this.x = x;
		this.y = y;
		this.maxLength = maxLength;
		this.key = key;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawRect(x, y, maxLength * 12, 18);
		Text.render(text + "_", x + 4, y + 14, 12, Font.BOLD, g);
	}

	@Override
	public void update() {
		if(key.a.pressed) press("a");
		if(key.b.pressed) press("b");
		if(key.c.pressed) press("c");
		if(key.d.pressed) press("d");
		if(key.e.pressed) press("e");
		if(key.f.pressed) press("f");
		if(key.g.pressed) press("g");
		if(key.h.pressed) press("h");
		if(key.i.pressed) press("i");
		if(key.j.pressed) press("j");
		if(key.k.pressed) press("k");
		if(key.l.pressed) press("l");
		if(key.m.pressed) press("m");
		if(key.n.pressed) press("n");
		if(key.o.pressed) press("o");
		if(key.p.pressed) press("p");
		if(key.q.pressed) press("q");
		if(key.r.pressed) press("r");
		if(key.s.pressed) press("s");
		if(key.t.pressed) press("t");
		if(key.u.pressed) press("u");
		if(key.v.pressed) press("v");
		if(key.w.pressed) press("w");
		if(key.x.pressed) press("x");
		if(key.y.pressed) press("y");
		if(key.z.pressed) press("z");		
				
		if(text.length() > 0){
			if(key.backSpace.pressed) text = text.substring(0, text.length() - 1);
		}
	}
	
	private void press(String msg){
		System.out.println("pressed");
		if(text.length() >= maxLength) return;
		text = text + msg;
	}

}

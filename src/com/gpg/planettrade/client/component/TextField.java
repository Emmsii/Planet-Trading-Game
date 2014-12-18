package com.gpg.planettrade.client.component;

import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Text;

public class TextField extends Component{
	
	public static final int JUST_NUMBERS = 0;
	public static final int JUST_LETTERS = 1;
	public static final int BOTH = 2;

	protected String text = "";
	protected int maxLength;
	protected int type;
	protected Keyboard key;
	
	public TextField(int x, int y, int maxLength, int type, Keyboard key){
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
		if(type == JUST_LETTERS) doLetters();
		if(type == JUST_NUMBERS) doNumbers();
		if(type == JUST_NUMBERS) doBoth();
							
		if(text.length() > 0) if(key.backSpace.pressed) text = text.substring(0, text.length() - 1);
			
		key.release();
	}
	
	private void doBoth(){
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
		if(key.space.pressed) press(" ");
		
		if(key.zero.pressed) press("0");
		if(key.one.pressed) press("1");
		if(key.two.pressed) press("2");
		if(key.three.pressed) press("3");
		if(key.four.pressed) press("4");
		if(key.five.pressed) press("5");
		if(key.six.pressed) press("6");
		if(key.seven.pressed) press("7");
		if(key.eight.pressed) press("8");
		if(key.nine.pressed) press("9");
	}
	
	private void doLetters(){
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
		if(key.space.pressed) press(" ");
	}
	
	private void doNumbers(){
		if(key.zero.pressed) press("0");
		if(key.one.pressed) press("1");
		if(key.two.pressed) press("2");
		if(key.three.pressed) press("3");
		if(key.four.pressed) press("4");
		if(key.five.pressed) press("5");
		if(key.six.pressed) press("6");
		if(key.seven.pressed) press("7");
		if(key.eight.pressed) press("8");
		if(key.nine.pressed) press("9");
	}
	
	private void press(String msg){
		if(text.length() >= maxLength) return;
		text = text + msg;
	}
	
	public String getText(){
		return text;
	}
	
	public void clear(){
		text = "";
	}
}

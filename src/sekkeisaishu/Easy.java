package sekkeisaishu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;


public class Easy {

	JFrame frame;
	BufferStrategy bstrategy;
	int x0=30/*雪だるまの初期位置*/, x1 = 290/*溶岩の位置*/, x2 = 120/*段差の位置*/, x3 = 50/*溶ける雪玉*/; 
	int y0=370/*雪だるまの初期位置*/, y1 = 380/*溶岩の位置*/,y2 = 340/*段差の高さ*/, y3 = 50/*溶ける雪玉*/;
	double x4 = 550/*流れる雲1*/, x5 = 150/*流れる雲2*/;
	double y4 = 100/*流れる雲1*/, y5 = 180/*流れる雲2*/;
	int x = x0;
	int y = y0;
	int yj,ys;//ジャンプ用パロメータとジャンプ開始地点記憶パロメータ
	int power = 0;//雪だるま初期状態
	int m = 0;//解けるのを防げる回数
	int q = 0;//溶岩を通過した回数
	int p = 0;//目印用
	boolean spacekey = false;
	boolean enterkey = false;
	boolean rightkey = false;
	boolean leftkey = false;
	Obstacle o = new Obstacle();

	Easy () {
		frame = new JFrame("MyWindow");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.BLACK);//Macでは背景色の変更できず
		frame.setResizable(false);
		frame.setVisible(true);
		Insets insets = frame.getInsets();
		frame.setSize(800+insets.left+insets.right,
				500+insets.top+insets.bottom);//構図を長方形に変更
		frame.setLocationRelativeTo(null);
		frame.setIgnoreRepaint(true);
		frame.createBufferStrategy(2);
		bstrategy = frame.getBufferStrategy();


		Timer t = new Timer();
		t.schedule(new MyTimerTask(), 10, 5);//30を5に(スピードの変更)

		frame.addKeyListener(new MyKeyAdapter());
	}

	class MyTimerTask extends TimerTask {
		public void run() {
			Graphics g = bstrategy.getDrawGraphics();
			if (bstrategy.contentsLost() == false) {
				Insets insets = frame.getInsets();
				g.translate(insets.left, insets.top);
				g.clearRect(0,0,800,500);//背景色で塗りつぶし
				
				g.setColor(Color.gray);
				g.fillOval((int)x4, (int)y4, 80, 60);//以下は雲1
				g.fillOval((int)x4+60, (int)y4, 60, 50);
				g.fillOval((int)x4+10, (int)y4+40,60,50);
				g.fillOval((int)x4+50, (int)y4+30, 80, 60);
				g.fillOval((int)x5, (int)y5, 80, 60);//以下は雲2
				g.fillOval((int)x5+60, (int)y5, 60, 50);
				g.fillOval((int)x5+10, (int)y5+40,60,50);
				g.fillOval((int)x5+50, (int)y5+30, 80, 60);
				g.setColor(Color.black);
				g.fillOval(x1, y1, 44, 40);//溶岩本体
				g.setColor(Color.RED);
				g.fillOval(x1+6, y1+3,15,18);//溶岩の模様1
				g.fillOval(x1+22, y1+18,18,15);//溶岩の模様2
				g.setColor(Color.orange);
				g.fillOval(x1+10, y1+5, 10, 8);//溶岩の模様1
				g.fillOval(x1+26, y1+20, 8, 10);//溶岩の模様1
				g.setColor(Color.green);//以下は地面
				g.fillRect(0,420,500,80);
				g.fillRect(600,420, 200, 80);
				g.fillRect(120, 340,100, 80);
				
				
				//////////////////雲を動かす//////////////////
				x4-=0.5;
				x5-=0.5;
				if(x4 < -130) {
					x4 = 800;
				}if(x5 < -130) {
					x5 = 800;
				}
				
				/////////////////左右移動/////////////////////
			 	if(o.judge(x, y) != 4) {
			 		if(o.judge(x,y) != 1 && leftkey == true) {
			 			x = x-1;//地上でleftkeyを押すと左へ移動
			 		}
			 		if(o.judge(x, y) != 2 && rightkey == true) {
			 			x = x+1;//地上でrightkeyを押すと右へ移動
			 		}
			 	}
			 	//////////////ジャンプ//////////////
				if(spacekey == true) {
					ys ++;
					if(ys == 1) {
						if(o.judge(x, y) == 3) {
							yj = y2-150;//段差の上にある時のジャンプの最大の高さ
						}else {
							yj = y0-100;//地上にいる時のジャンプの最大の高さ
						}
					}
					if(y > yj && p == 0) {//ジャンプ力の限界値を設定
						y -= 2;
					}else {//限界値に到達した目印をつける
						p = 1;
					}if(o.judge(x, y) == 3) {
						if(p == 1 && y < y2-50) {
							y += 2;
						}
					}else {
						if(p == 1 && y < y0) {//落下
								y += 2;
						}
					}
				}
				if(spacekey == false) {
					p = 0;
					if(y < y0) {
						ys = 0;
						if(o.judge(x, y) == 3) {//台の上に乗っている時はその台の上に着地するように
							if(y < y2 - 50) {
								y += 2;
							}
						}else {//地面なら地面に着地，穴の場合落下
								y += 2;
						}
					}
				}
				//////////////初期状態に戻す////////////////
				if(y > 580 || enterkey == true || y3 < 0 || x > 800) {//落下した，クリアした，またはenterを入力した際に初期状態に戻す
					x = x0;
					y = y0;
					y3 = 50;
					power = 0;
				}
				//////////////巨大化//////////////////
					if(power == 0 && (x > x5 && x < x5 + 140 && y > y5 && y < y5 + 120)||(x > x4 && x < x4 + 140 && y > y4 && y < y4 + 120)) {
						power = 1;
						m = 1;
					} if(x < x1 - 50 && x < x1 - 60 && x > x1 + 44 && x1 > x1 + 54) {//画面上から一瞬雪だるまが消えるのを防ぐ
						g.setColor(Color.cyan);
						g.fillOval(x,y,50,50);//雪玉大
						g.fillOval((int)(x+7.5),y-27,35,35);//雪玉小
						g.setColor(Color.BLACK);
						g.fillOval(x+25,y-17,10,10);//目の部分
						g.setColor(Color.RED);
						g.fillRect(x+20,y-36,10,10);//頭のバケツ
					} if(x > x1 - 50 &&  x < x1 + 44 && y > y1 - 50) {
						
						if(power == 0) {//最初から小さい状態でぶつかった時溶ける
							if(m == 0) {
								g.setColor(Color.red);
								String melt = "I'm melting !!";
								g.setFont(new Font("SansSerif",Font.BOLD,10));
								g.drawString(melt, x1, y1-50);
								leftkey = false;
								spacekey = false;	
								rightkey = false;
								g.setColor(Color.pink);
								g.fillOval(x,y,x3,y3);		
								g.fillOval((int)(x+7.5),y-27,x3-15,y3-15);
								g.setColor(Color.BLACK);
								g.fillOval(x+25,y-17,10,10);
								g.setColor(Color.RED);
								g.fillRect(x+20,y-36,10,10);
								y3-=0.5;
								y+=1;
							}else /*(m == 1)*/{
								g.setColor(Color.yellow);
							g.fillOval(x,y,50,50);//雪玉大
							g.fillOval((int)(x+7.5),y-27,35,35);//雪玉小
							g.setColor(Color.BLACK);
							g.fillOval(x+25,y-17,10,10);//目の部分
							g.setColor(Color.RED);
							g.fillRect(x+20,y-36,10,10);//頭のバケツ
							}
						}if(power == 1) {//巨大化した状態で溶岩にぶつかると小さくなる
							q = 1;//溶岩にぶつかったことを記憶
							power = 0;
						}
					}else if((x < x1 - 60 ||  x > x1 + 54) && q == 1) {//溶岩に一度ぶつかり縮んだ雪だるま
						m = 0;//もう避けることができない
						q = 0;
					}else if(power == 0) {
						/*解けなかった時の雪だるま*/
						g.setColor(Color.cyan);
						g.fillOval(x,y,50,50);//雪玉大
						g.fillOval((int)(x+7.5),y-27,35,35);//雪玉小
						g.setColor(Color.BLACK);
						g.fillOval(x+25,y-17,10,10);//目の部分
						g.setColor(Color.RED);
						g.fillRect(x+20,y-36,10,10);//頭のバケツ
					}else /*if(power == 1)*/{
						g.setColor(Color.cyan);
						g.fillOval(x-10,y-10,60,60);//雪玉大
						g.fillOval((int)(x+7.5)-10,y-27-19,45,45);//雪玉小
						g.setColor(Color.BLACK);
						g.fillOval(x+25-8,y-17-18,15,15);//目の部分
						g.setColor(Color.RED);
						g.fillRect(x+20-5,y-36-19,10,10);//頭のバケツ
					}

				/////////////////////落下////////////////////////
				if(x > 480 && x < 570 && y >= y0) {/*穴に差し掛かると落下*/
					y = y+1;
					g.setColor(Color.red);
					String fall = "Oh No !!";
					g.setFont(new Font("SansSerif",Font.BOLD,10));
					g.drawString(fall, x+10, y-50);
					leftkey = false;/*後ろに下がって落ちないようにするバグを防止*/
					spacekey = false;/*落ちている最中にジャンプできるバグを防止*/
					if(x >= 550 && rightkey == true) {/*勢い余って壁にめり込むことを防止*/
						x = x-1;
					}
				}
				/////////////////クリアした時の表示////////////////
				if(x > 720-50) {
					g.setColor(Color.gray);
					String clear = "clear!!";
					g.setFont(new Font("SansSerif",Font.BOLD,50));
					g.drawString(clear, 200, 300);
					if(x > 800) {
						
					}
				}
				//////////////説明文の表示////////////////
				g.setColor(Color.black);
				String game = "Go!Go!SnowMan";
				g.setFont(new Font("SansSerif",Font.BOLD,15));
				g.drawString(game, 5, 20);
				String comment = "Can you reach the goal?";
				g.drawString(comment, 5, 40);
				String rule = "rightkey; '->'    leftkey; '<-'    spacekey; 'jump'    enterkey; 'reset'";
				g.drawString(rule, 5, 60);
				String warning = "Don't touch the lava!";
				g.drawString(warning, 5, 80);
				g.setFont(new Font("SansSerif",Font.BOLD,10));
				String lava = "lava";
				g.drawString(lava, x1+10, y1-5);
				String danger = "Danger↓";
				g.drawString(danger, x1, y1-20);
				g.fillRect(700,240,100,180);
				g.setColor(Color.orange);
				String goal = "Goal";
				g.setFont(new Font("SansSerif",Font.BOLD,30));
				g.drawString(goal, 720, 320);
				bstrategy.show();
				g.dispose();
			}
		}
	}
	////////////////キーの設定///////////////
	class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				spacekey=true;
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				enterkey=true;
			}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightkey=true;
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftkey=true;
			}
		}
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				spacekey=false;
			}else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				enterkey=false;
			}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightkey=false;
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftkey=false;
			}
		}
	}
}


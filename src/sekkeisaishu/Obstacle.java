package sekkeisaishu;

public class Obstacle {
	public Obstacle() {
	}
	
	public int judge(int x, int y) {
		
		if(x < 0) { //画面外
			return 1;//leftのみ防止
		}if(y <= 370 && y > 290) {
			if(x == 120-50) {
				return 2;//right防止
			}if(x == 220) {
				return 1;//left防止
			}
		}if(x > 120-50 && x < 220) {//段差の上の値
			if(y >= 290) {
				return 3;//段差の上に乗る
			}
		}if(x > 120-50 && x < 220 && y > 290) {
				return 4;//両方防止
		}
		return 0;
	}
}

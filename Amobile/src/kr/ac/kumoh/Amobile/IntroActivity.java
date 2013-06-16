package kr.ac.kumoh.Amobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class IntroActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_layout);
		Animation an = new AlphaAnimation(0.0f, 1.0f);
		an.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				Handler h = new Handler();
				h.postAtTime(new Runnable() {

					public void run() {
						startActivity(new Intent(IntroActivity.this,
								MainActivity.class));

						finish();
					}
				}, 3000);
			}
		});
		an.setDuration(5000);
		ImageView introImage = (ImageView) findViewById(R.id.IntroImage);
		introImage.setAnimation(an);
	}
}
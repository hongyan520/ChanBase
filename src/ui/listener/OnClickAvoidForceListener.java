package ui.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.demo.base.util.ClickEffectUtil;

 

public abstract class OnClickAvoidForceListener implements OnClickListener
{

	public void onClick(View v)
	{
		ClickEffectUtil util = ClickEffectUtil.getInstance(); 
		if (util.isSmoothClick())
		{
		 
			util.playAnimation(v);
			onClickAvoidForce(v);
		}
		else
		{
			ClickEffectUtil.getInstance().slowDownDialog(v.getContext());
		}
	}

	public abstract void onClickAvoidForce(View v);
}

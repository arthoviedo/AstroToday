package smarthur.space.astrotoday;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                HomeFragment fragment = (HomeFragment)
                        navHostFragment.getChildFragmentManager().getFragments().get(0);

                if (fragment != null) {
                    Log.e("MyTest", "onClick: ok" );
                    fragment.update();
                } else {
                    Log.e("MyTest", "onClick: couldnt find fragment" );
                }
            }
        });
    }
}
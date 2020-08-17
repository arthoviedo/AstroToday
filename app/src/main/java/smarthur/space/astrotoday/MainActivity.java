package smarthur.space.astrotoday;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

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
                FirstFragment fragment = (FirstFragment)
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
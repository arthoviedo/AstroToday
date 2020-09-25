package smarthur.space.astrotoday;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.MenuItem;
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
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
                UpdatableFragment fragment = (UpdatableFragment)
                        navHostFragment.getChildFragmentManager().getFragments().get(0);

                if (fragment != null) {
                    fragment.update();
                } else {
                }
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
                switch (item.getItemId()) {
                    case R.id.planets:
                        navHostFragment.getNavController().navigate(R.id.planets_fragment);
                        break;
                    case R.id.planetary_nebulae:
                        navHostFragment.getNavController().navigate(R.id.planetary_nebulae_fragment);
                        break;
                    case R.id.galaxies:
                        navHostFragment.getNavController().navigate(R.id.galaxies_fragment);
                        break;
                }
                return true;
            }
        });
    }
}



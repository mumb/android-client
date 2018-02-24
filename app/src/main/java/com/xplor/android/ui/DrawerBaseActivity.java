package com.xplor.android.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xplor.android.R;
import com.xplor.android.models.User;
import com.xplor.android.ui.activities.LeaderBoardActivity;
import com.xplor.android.ui.activities.LoginActivity;
import com.xplor.android.ui.activities.ProfileActivity;
import com.xplor.android.utils.Functions;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class DrawerBaseActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private CircleImageView imgNavHeaderProfile;
    private TextView txtNavHeaderName;
    private TextView txtNavHeaderPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsMenu();
        navigationView.setNavigationItemSelectedListener(this);
        if (User.getInstance().isLoggedIn()) {
            navigationView.getMenu().findItem(R.id.nav_log_in).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.nav_log_out).setVisible(false);
        }
        View headerLayout = navigationView.getHeaderView(0);
        imgNavHeaderProfile = headerLayout.findViewById(R.id.imgNavHeaderProfile);
        txtNavHeaderName = headerLayout.findViewById(R.id.txtNavHeaderName);
        txtNavHeaderPoints = headerLayout.findViewById(R.id.txtNavHeaderPoints);

        User user = User.getInstance();
        if (!user.isLoggedIn()) {
            txtNavHeaderName.setText(R.string.user_name_guest);
            txtNavHeaderPoints.setText(getString(R.string.points_earned, "0"));
        } else {
            Functions.loadImageIntoCircularImageView(this, Functions.getPhotoUrl(user.getFacebookId()), imgNavHeaderProfile);
            txtNavHeaderName.setText(user.getName());
            txtNavHeaderPoints.setText(getString(R.string.points_earned, String.valueOf(user.getPoints())));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getInstance();
        if (user.isLoggedIn()) {
            txtNavHeaderPoints.setText(getString(R.string.points_earned, String.valueOf(user.getPoints())));
        } else {
            txtNavHeaderPoints.setText(getString(R.string.points_earned, "0"));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                if (!User.getInstance().isLoggedIn()) {
                    Functions.showToast(this, R.string.need_to_log_in_to_see_profile);
                } else {
                    ProfileActivity.startActivity(this);
                }
                break;
            case R.id.nav_leader_board:
                if (!User.getInstance().isLoggedIn()) {
                    Functions.showToast(this, R.string.need_to_log_in);
                } else {
                    LeaderBoardActivity.startActivity(this);
                }
                break;
            case R.id.nav_log_in:
                LoginActivity.startActivity(this);
                break;
            case R.id.nav_log_out:
                logoutUser();
                break;
        }
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_base_drawer;
    }

    @OnClick(R.id.img_home_action)
    void onClick(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

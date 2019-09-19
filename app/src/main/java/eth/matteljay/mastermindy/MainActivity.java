package eth.matteljay.mastermindy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private GameModel gameModel;
    private PersistVM persistVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        persistVM = ViewModelProviders.of(this).get(PersistVM.class);

        // Remove 'drag-return' animation
        View theContentView = findViewById(android.R.id.content);
        theContentView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });

        gameModel = new GameModel(this);
        gameModel.readSettings();
        if(gameModel.isScreenKeepOn()) {
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        gameModel.BuildUI();
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameModel.processTurnEvent();
            }
        });
        findViewById(R.id.menufab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                MainActivity.this.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();
            }
        });
        if(savedInstanceState == null) {
            gameModel.freshGame();
        }
    }

    @Override
    protected void onPause() {
        gameModel.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameModel.resumeGame();
    }

    public void resetGame() {
        gameModel.pauseGame();
        gameModel.BuildUI();
        gameModel.freshGame();
        gameModel.resumeGame();
        gameModel.showToast(getString(R.string.new_game));
    }

    public boolean isGameFrozen() {
        return persistVM.isGameFrozen;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.exit_dialog_title))
                .setMessage(getString(R.string.exit_dialog_body))
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                })
                .show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_reveal_secret:
                gameModel.endGame(getString(R.string.game_ended));
                return true;
            case R.id.action_restart:
                resetGame();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_save_exit:
                gameModel.pauseGame();
                new SaveGame(this).Save();
                finishAndRemoveTask();
                return true;
            default:
                return false;
        }
    }
}

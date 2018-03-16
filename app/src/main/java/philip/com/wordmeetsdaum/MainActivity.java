package philip.com.wordmeetsdaum;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import philip.com.wordmeetsdaum.model.Word;

import static philip.com.wordmeetsdaum.MyApplication.MyDb;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private RecyclerView mRecyclerView;
    private MyWordRecyclerViewAdapter mAdapter;
    private boolean isShowingMyWords = true;

    private OnListInteractionListener mListener = new OnListInteractionListener() {
        @Override
        public void onClick(int index, String word) {
            savePreference(index);
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra(Constant.EXTRA_WORD, word);
            startActivity(intent);
        }

        @Override
        public void onChecked(int index, Word word) {
            updateData(index, word);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
        fetchData(isShowingMyWords);
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.scrollToPosition(0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new MyWordRecyclerViewAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchData(boolean showMyWord) {
        Single<List<Word>> single;
        if (showMyWord) {
            single = MyDb.wordDao().loadMyWords();
        } else {
            single = MyDb.wordDao().loadWords();
        }

        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Word>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Word> words) {
                mAdapter.setWords(words);
                mRecyclerView.scrollToPosition(getPreference());
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void saveData(String words, boolean isChecked) {
        String[] wordArray = words.split("\n");
        final List<Word> wordList = new ArrayList<>();
        for (String word : wordArray) {
            wordList.add(new Word(word, isChecked));
        }

        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                MyDb.wordDao().insertWords(wordList);
                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                fetchData(isShowingMyWords);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void updateData(final int index, final Word word) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                MyDb.wordDao().updateWord(word);
                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (isShowingMyWords) {
                    mAdapter.removeWord(index);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private int getPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("WordMeetsDaum", MODE_PRIVATE);
        if (isShowingMyWords) {
            return sharedPreferences.getInt("myIndex", 0);
        } else
            return sharedPreferences.getInt("index", 0);
    }

    private void savePreference(int index) {
        SharedPreferences sharedPreferences = getSharedPreferences("WordMeetsDaum", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isShowingMyWords)
            editor.putInt("myIndex", index);
        else
            editor.putInt("index", index);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_mywords) {
            if (isShowingMyWords)
                return true;

            isShowingMyWords = true;
            fetchData(isShowingMyWords);
        } else if (id == R.id.nav_wordsnote) {
            if (!isShowingMyWords)
                return true;

            isShowingMyWords = false;
            fetchData(isShowingMyWords);
        } else if (id == R.id.nav_addwords) {
            Intent intent = new Intent(MainActivity.this, InputActivity.class);
            startActivityForResult(intent, Constant.REQUEST_INPUT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == Constant.REQUEST_INPUT) {
            saveData(data.getStringExtra(Constant.EXTRA_INPUT_WORDS), data.getBooleanExtra(Constant.EXTRA_ADD_TO_MYWORDS, false));
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }
}

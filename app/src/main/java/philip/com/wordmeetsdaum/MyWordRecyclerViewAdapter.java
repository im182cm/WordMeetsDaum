package philip.com.wordmeetsdaum;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import philip.com.wordmeetsdaum.model.Word;

import java.util.ArrayList;
import java.util.List;

public class MyWordRecyclerViewAdapter extends RecyclerView.Adapter<MyWordRecyclerViewAdapter.ViewHolder> implements Filterable{

    private List<Word> mWords = new ArrayList<>();
    private List<Word> mFilteredWords = new ArrayList<>();
    private final OnListInteractionListener mListener;

    public MyWordRecyclerViewAdapter(OnListInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position, mFilteredWords.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilteredWords.size();
    }

    public void setWords(List<Word> words) {
        mWords = words;
        mFilteredWords = mWords;
        notifyDataSetChanged();
    }

    public void removeWord(int index){
        Word word = mFilteredWords.get(index);
        mFilteredWords.remove(index);
        mWords.remove(word);
        notifyItemRemoved(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredWords = mWords;
                    Log.d("TEST", mFilteredWords.toString());
                } else {
                    List<Word> filteredList = new ArrayList<>();
                    for (Word word : mWords) {
                        if (word.toString().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(word);
                        }
                    }

                    mFilteredWords = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredWords;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredWords = (List<Word>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mTextView;
        private final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = view.findViewById(R.id.text_word);
            mCheckBox = view.findViewById(R.id.checkbox);
        }

        private void bind(final int position, final Word item) {
            mCheckBox.setOnCheckedChangeListener(null);

            mTextView.setText(item.toString());
            mCheckBox.setChecked(item.isChecked());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(mWords.indexOf(item), item.getContent());
                }
            });

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.mIsChecked = isChecked;
                    mWords.set(mWords.indexOf(item), item);
                    mListener.onChecked(position, item);
                }
            });
        }
    }
}

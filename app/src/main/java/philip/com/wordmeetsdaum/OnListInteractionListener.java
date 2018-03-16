package philip.com.wordmeetsdaum;

import philip.com.wordmeetsdaum.model.Word;

/**
 * Created by 1000140 on 2018. 3. 13..
 */

public interface OnListInteractionListener {

    void onClick(int index, String word);

    void onChecked(int index, Word word);
}

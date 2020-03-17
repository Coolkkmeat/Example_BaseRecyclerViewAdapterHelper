package net.coolkk.test;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<MainAdapterData, BaseViewHolder> implements LoadMoreModule, DraggableModule {

    MainAdapter(int layoutResId, List<MainAdapterData> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainAdapterData item) {
        helper.setText(R.id.textView, item.getTitle())
                .setText(R.id.button, item.getInfo());
    }
}

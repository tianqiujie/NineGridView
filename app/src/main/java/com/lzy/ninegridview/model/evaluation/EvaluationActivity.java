package com.lzy.ninegridview.model.evaluation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lzy.ninegridview.R;
import com.lzy.ninegridview.model.evaluation.bean.BaseData;
import com.lzy.ninegridview.model.evaluation.bean.Evaluation;
import com.lzy.ninegridview.model.evaluation.bean.EvaluationItem;
import com.lzy.ninegridview.utils.Urls;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.BeanCallBack;
import com.lzy.okhttputils.model.RequestParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class EvaluationActivity extends AppCompatActivity {

    @Bind(R.id.ptr) PtrClassicFrameLayout ptr;
    @Bind(R.id.listView) ListView listView;

    private EvaluationAdapter mAdapter;
    private ArrayList<EvaluationItem> data;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        ButterKnife.bind(this);

        View emptyView = View.inflate(this, R.layout.item_empty, null);
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setEmptyView(emptyView);

        mAdapter = new EvaluationAdapter(this, new ArrayList<EvaluationItem>());
        listView.setAdapter(mAdapter);

        initData(false);

        ptr.setLastUpdateTimeRelateObject(this);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData(true);
            }
        });
    }

    private void initData(final boolean isMore) {
        RequestParams params = new RequestParams();
        params.put("goodsId", "98573");
        params.put("pageNo", String.valueOf(page));
        OkHttpUtils.post(Urls.Evaluation).tag(this).params(params).execute(new BeanCallBack<BaseData<Evaluation>>() {
            @Override
            public void onResponse(BaseData<Evaluation> evaluationBaseData) {
                if (isMore) {
                    data.addAll(0, evaluationBaseData.getData().getEvaluataions());
                } else {
                    data = evaluationBaseData.getData().getEvaluataions();
                }
                mAdapter.setData(data);
                page++;
                ptr.refreshComplete();
            }
        });
    }
}
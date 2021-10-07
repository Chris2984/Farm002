package tw.tcfarmgo.tcnrcloud110a01;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Q0501_RecyclerAdapter extends RecyclerView.Adapter<Q0501_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Q0501_Post> mData;
    //    -------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener = null;
    //--------------------------------------------
    public Q0501_RecyclerAdapter(Context context, ArrayList<Q0501_Post> data) {
        this.mContext = context;
        this.mData = data;
    }
    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //-------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.q0501_cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.q0501_img);//correspond to layout
        holder.Name = (TextView) view.findViewById(R.id.q0501_t201);
        holder.Content = (TextView) view.findViewById(R.id.Content);
        holder.Add = (TextView) view.findViewById(R.id.q0501_t205);
        holder.Zipcode = (TextView) view.findViewById(R.id.q0501_t204);
        holder.TEL = (TextView) view.findViewById(R.id.q0501_t202);
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Q0501_Post post = mData.get(position);
        holder.Name.setText(post.FarmNm_CH);
        //holder.Website.setText("網址:"+post.Website);
//        if (post.Parkinginfo_Px.length()>0 && post.Parkinginfo_Py.length()>0){ //如果有網站
//            holder.Parkinginfo_Px.setText("停車場:"+post.Parkinginfo_Px+",");
//            holder.Parkinginfo_Py.setText(post.Parkinginfo_Py);
//        }else{//沒有網站
//            //holder.Website.setText("[未提供網站]");
//            holder.Parkinginfo_Px.setText("未提供停車場地址");
//            holder.Parkinginfo_Py.setText("");
//        }
//        holder.Px.setText("經度:"+post.Longitude+",");
//        holder.Py.setText("緯度:"+post.Latitude);
        holder.TEL.setText("Tel:"+post.TEL);
        holder.Add.setText(post.Address_CH);
        holder.Content.setText(post.Feature_CH);

        if (post.PCode.length()>0){ //如果有郵遞區號
            holder.Zipcode.setText("["+post.PCode+"]");
        }else{//沒有有郵遞區號
            holder.Zipcode.setText("[000]");
        }
//==//        若圖片檔名是中文無法下載,可用此段檢查圖片網址且將中文解碼
////        String ans_Url = post.posterThumbnailUrl;
////        if (post.posterThumbnailUrl.getBytes().length == post.posterThumbnailUrl.length() ||
////                post.posterThumbnailUrl.getBytes().length > 100) {
////            ans_Url = post.posterThumbnailUrl;//不包含中文，不做處理
////        } else {
//////    ans_Url = utf8Togb2312(post.posterThumbnailUrl);
//////           ans_Url = utf8Togb2312(post.posterThumbnailUrl).replace("http://", "https://");
////        }
////        Glide.with(mContext)
////                .load(ans_Url)
////                .into(holder.img);
////----------------------------------------
//        if (post.Website.length() > 0) {
//            holder.Website.setText("" + QQ + ""); // 如果有 Website 就顯示
//        } else {
//            holder.Website.setText("無網站"); // 如果沒有 Website 就顯示 無網站
//        }
// ==========================================//posterThumbnailUrl來自於Post
        Glide.with(mContext)
                .load(post.Photo)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(100, 75) //圖片重新調整大小
                .transition(withCrossFade())
                .error(
                        Glide.with(mContext)
                                .load("https://tcnr2021a11.000webhostapp.com/post_img/nopic1.jpg")) //無提供照片時
                .into(holder.img);

        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }

    ////    //    -----------把中文字符轉換為帶百分號的瀏覽器編碼-----------
//    public static String utf8Togb2312(String inputstr) {
//        String r_data = "";
//        try {
//            for (int i = 0; i < inputstr.length(); i++) {
//                char ch_word = inputstr.charAt(i);
////            下面這段代碼的意義是:只對中文進行轉碼
//                if (ch_word + "".getBytes().length > 1 && ch_word != ':' && ch_word != '/') {
//                    r_data = r_data + java.net.URLEncoder.encode(ch_word + "", "utf-8");
//                } else {
//                    r_data = r_data + ch_word;
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } finally {
////            System.out.println(r_data);
//        }
//        return r_data;
//    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView Name;
        public TextView Add;
        public TextView Content;
        public TextView Zipcode;
        //public TextView Website;
        //public TextView Px,Py;
        public TextView TEL;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
//-----------------------------------------------
}

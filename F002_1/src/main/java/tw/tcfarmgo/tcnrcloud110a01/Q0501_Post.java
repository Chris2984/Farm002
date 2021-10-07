package tw.tcfarmgo.tcnrcloud110a01;

public class Q0501_Post {
    public String Address_CH;
    public String FarmNm_CH;
    public String Photo;
    public String Feature_CH;
    public String PCode;
    public String Longitude;
    public String Latitude;
    public String WebURL;
    public String TEL;


    public Q0501_Post(String farmNm_ch, String photo, String address_ch, String feature_ch, String pCode, String longitude, String latitude, String webURL, String tel) {
        this.FarmNm_CH = farmNm_ch;  //名稱
        this.Photo = photo; //圖片
        this.Address_CH = address_ch;  //住址
        this.Feature_CH = feature_ch; //描述
        this.PCode = pCode; //郵遞區號
        this.Longitude=longitude;
        this.Latitude=latitude;
        this.WebURL=webURL;
        this.TEL=tel;
    }

//    public Q0501_Post(String address_ch, String longitude, String latitude, String webURL, String tel) {
//        this.Address_CH = address_ch;  //住址
//        this.Longitude=longitude;
//        this.Latitude=latitude;
//        this.WebURL=webURL;
//        this.TEL=tel;
//    }

    public Q0501_Post(String b_address_ch, String b_longitude, String b_latitude, String b_webURL, String b_tel, String b_farmNm_ch) {
        this.Address_CH = b_address_ch;  //住址
        this.Longitude=b_longitude;
        this.Latitude=b_latitude;
        this.WebURL=b_webURL;
        this.TEL=b_tel;
        this.FarmNm_CH =b_farmNm_ch;
    }
}

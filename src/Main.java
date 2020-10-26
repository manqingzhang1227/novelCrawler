import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;


public class Main {

    public static void main( String[] args ) throws Exception {

        //douban
        // 个人中心url
        String user_info_url = "https://www.douban.com/people/77850005/";

        // 登陆接口
        String login_url = "https://www.amazon.com/ap/signin";
        //"https://accounts.douban.com/j/mobile/login/basic";

        String orders_info_url = "https://www.amazon.com/gp/css/order-history?ref_=nav_orders_first";

        //new Crawler().jsoupLoginSephora(orders_info_url,orders_info_url);

        // new CrawleLogin().setCookies(user_info_url);
        //new Crawler().jsoupLogin(login_url,user_info_url);



        // # Constants used in this example
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
        final String LOGIN_FORM_URL = "https://m.jfchinese.com/login/loginPage.do?timestamp=1599936072512&sign=Ft3zAO8Um3cx3JhkRH/KGg==&uuid=3027c249-102a-4735-9596-dfee117ee21a";
        final String USERNAME = "Zmq张曼晴";
        final String PASSWORD = "15901131227";
        Connection.Response loginResponse = Jsoup.connect( LOGIN_FORM_URL )
            .userAgent( USER_AGENT ).data( "LoginAcct", USERNAME )
            .data( "LoginPwd", PASSWORD ).method( Connection.Method.POST )
            .execute();

        System.out.println( loginResponse.parse() );

    }


    public static void checkElement( String name, Element elem ) {
        if( elem == null ) {
            throw new RuntimeException( "Unable to find " + name );
        }
    }

   /* public static void main(String[] args) {
        String url = "https://voice.hupu.com/nba";
        Crawler crawlerBase = new Crawler();
        crawlerBase.jsoupList(url);
    }*/



    //public static DB db = new DB();

    /*public static void main(String[] args) throws SQLException, IOException {
        db.runSql2("TRUNCATE Record;");
        processPage("http://www.mit.edu");
    }

    public static void processPage(String URL) throws SQLException, IOException{
        //check if the given URL is already in database
        String sql = "select * from Record where URL = '"+URL+"'";
        ResultSet rs = db.runSql(sql);
        if(rs.next()){

        }else{
            //store the URL to database to avoid parsing again
            sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " +
                "(?);";
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, URL);
            stmt.execute();

            //get useful information
            Document doc = Jsoup.connect("http://www.mit.edu/").get();

            if(doc.text().contains("research")){
                System.out.println(URL);
            }

            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for(Element link: questions){
                if(link.attr("href").contains("mit.edu"))
                    processPage(link.attr("abs:href"));
            }
        }
    }*/
}


/*
*Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
                .maxBodySize(0)
                .get();
Elements products = doc.select(".s-result-list-parent-container > ul > li"); */
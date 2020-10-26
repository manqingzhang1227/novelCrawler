import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Crawler {

    public void jsoupLoginAmazon( String loginUrl, String userInfoUrl )
        throws IOException {

        // 构造登陆参数
        Map<String, String> data = new HashMap<>();
        data.put( "name", "your_account" );
        data.put( "password", "your_password" );
        data.put( "remember", "false" );
        data.put( "ticket", "" );
        data.put( "ck", "" );
        Connection.Response login = Jsoup.connect( loginUrl )
            .ignoreContentType( true ) // 忽略类型验证
            .followRedirects( false ) // 禁止重定向
            .postDataCharset( "utf-8" )
            .header( "Upgrade-Insecure-Requests", "1" )
            .header( "Accept", "application/json" )
            .header( "Content-Type", "application/x-www-form-urlencoded" )
            .header( "X-Requested-With", "XMLHttpRequest" )
            .header( "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36" )
            .data( "email", "994046326@qq.com", "password", "15901131227" )
            .method( Connection.Method.POST ).execute();
        login.charset( "UTF-8" );
        // login 中已经获取到登录成功之后的cookies
        // 构造访问个人中心的请求
        Document document = Jsoup.connect( userInfoUrl )
            // 取出login对象里面的cookies
            .cookies( login.cookies() ).get();
        if( document != null ) {
            //Element element = document.select(".a-box-inner h1").first();
            Element element = document.select( ".info h1" ).first();
            if( element == null ) {
                System.out.println( document + "\n没有找到 .info h1 标签" );
                return;
            }
            String userName = element.ownText();
            System.out.println( "豆瓣我的网名为：" + userName );
        }
        else {
            System.out.println( "出错啦！！！！！" );
        }
    }


    /**
     * Jsoup 模拟登录豆瓣 访问个人中心
     * 在豆瓣登录时先输入一个错误的账号密码，查看到登录所需要的参数
     * 先构造登录请求参数，成功后获取到cookies
     * 设置request cookies，再次请求
     *
     * @param loginUrl    登录url
     * @param userInfoUrl 个人中心url
     * @throws IOException
     */
    public void jsoupLogin( String loginUrl, String userInfoUrl )
        throws IOException {

        // 构造登陆参数
        Map<String, String> data = new HashMap<>();
        data.put( "name", "your_account" );
        data.put( "password", "your_password" );
        data.put( "remember", "false" );
        data.put( "ticket", "" );
        data.put( "ck", "" );
        Connection.Response login = Jsoup.connect( loginUrl )
            .ignoreContentType( true ) // 忽略类型验证
            .followRedirects( false ) // 禁止重定向
            .postDataCharset( "utf-8" )
            .header( "Upgrade-Insecure-Requests", "1" )
            .header( "Accept", "application/json" )
            .header( "Content-Type", "application/x-www-form-urlencoded" )
            .header( "X-Requested-With", "XMLHttpRequest" )
            .header( "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36" )
            .data( data ).method( Connection.Method.POST ).execute();
        login.charset( "UTF-8" );
        // login 中已经获取到登录成功之后的cookies
        // 构造访问个人中心的请求
        Document document = Jsoup.connect( userInfoUrl )
            // 取出login对象里面的cookies
            .cookies( login.cookies() ).get();
        if( document != null ) {
            Element element = document.select( ".info h1" ).first();
            if( element == null ) {
                System.out.println( document + "\n没有找到 .info h1 标签" );
                return;
            }
            String userName = element.ownText();
            System.out.println( "豆瓣我的网名为：" + userName );
        }
        else {
            System.out.println( "出错啦！！！！！" );
        }
    }


    /**
     * jsoup方式 获取虎扑新闻列表页
     *
     * @param url 虎扑新闻列表页url
     */
    public void jsoupList( String url ) {
        try {
            Document document = Jsoup.connect( url ).get();
            // 使用 css选择器 提取列表新闻 a 标签
            // <a href="https://voice.hupu.com/nba/2484553.html" target="_blank">霍华德：夏休期内曾节食30天，这考验了我的身心</a>
            Elements elements = document
                .select( "div.news-list > ul > li > div.list-hd > h4 > a" );
            for( Element element : elements ) {
                //                System.out.println(element);
                // 获取详情页链接
                String d_url = element.attr( "href" );
                // 获取标题
                String title = element.ownText();

                System.out.println( "详情页链接：" + d_url + " ,详情页标题：" + title );

            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}

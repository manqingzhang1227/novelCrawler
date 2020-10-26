import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;


/**
 * Example program to list links from a URL.
 */
public class TXTDownloader {
    //css selector e.g -> https://jsoup.org/cookbook/extracting-data/selector-syntax

    static File file;
    //    static String titleKeepFront = " - ";
    //    static String titleKeepLast = "";

    //    static String novel = "天字嫡一号";
    //    static String author = "青铜穗";
    //    static String sourceUrl = "http://www.aishuzw.com/modules/article/reader.php?aid=26350";
    //    static String listCssQuery = "div#BookText a[href]";
    //    static int skipN = 0;
    //    static String contentCssQuery = "div#booktext";

    //    static String novel = "局外人";
    //    static String author = "上山";
    //    static String sourceUrl = "http://www.jjwxc.net/onebook.php?novelid=3932733";
    //    static String listCssQuery = "tbody a[href][itemprop]";
    //    static int skipN = 0;
    //    static String contentCssQuery = "div.noveltext";
    //    static String dir = "htm";



    //    static String novel = "每次快穿睁眼都在被啪啪（NP）";
    //    static String author = "咖啡因";
    //    static String baseUrl = "http://www.50shubao.com";
    //    static String sourceUrl = "http://www.50shubao.com/9/9718/";
    //    static String listCssQuery = "div.liebiao a[href]";
    //    static int skipN = 0;
    //    static String contentCssQuery = "div#content p";
    //    static String dir = "";


    static String novel = "白月光回来以后（失控沦陷）";
    static String author = "无影有踪";
    static String baseUrl = "https://www.bxwx66.com/";
    static String sourceUrl = "https://www.bxwx66.com/read/126479/";
    static String listCssQuery = "div#list >dl > dd > a";
    static int skipN = 13;
    static String contentCssQuery = "#content> p";
    static String dir = "";


    //    .class	.intro	Selects all elements with class="intro"
    //.class1.class2	.name1.name2	Selects all elements with both name1 and name2 set within its class attribute
    //.class1 .class2	.name1 .name2	Selects all elements with name2 that is a descendant of an element with name1
    //#id

    static ArrayList<Integer> chapNums;
    static ArrayList<File> chapFiles;
    static File dirList;


    public int run() throws IOException {

        file = new File( author + " - " + novel + ".txt" );
        System.out.println( file.getName() + " downloading starts." );


        chapNums = new ArrayList<>();
        chapFiles = new ArrayList<>();
        if( dir.length() != 0 ) {
            dirList = new File( dir );
            for( File f : dirList.listFiles() ) {
                int chapNum = Integer.parseInt( f.toString()
                    .substring( f.toString().lastIndexOf( "/" ) + 1,
                        f.toString().lastIndexOf( "." ) ) );
                chapNums.add( chapNum );
                chapFiles.add( f );
            }
        }
        //        System.out.println( chapFiles );


        Elements links = getChapterUrlListCss();
        System.out.println( links );
        System.out.println( "Confirm chapters (y/n): " );

        BufferedReader reader = new BufferedReader(
            new InputStreamReader( System.in ) );

        if( reader.readLine().equals( "n" ) ) {
            return 1;
        }


        downloadTxt( links );
        System.out.println( "TXT downloading finished." );



        return 0;
    }


    public TXTDownloader( String novel, String author, String baseUrl,
        String sourceUrl, String listCssQuery, int skipN,
        String contentCssQuery ) {
        this.novel = novel;
        this.author = author;
        this.baseUrl = baseUrl;
        this.sourceUrl = sourceUrl;
        this.listCssQuery = listCssQuery;
        this.skipN = skipN;
        this.contentCssQuery = contentCssQuery;
    }


    public static void downloadTxt( Elements links ) {
        //        String sourceUrl,String novel, String author, int skipN) {

        int num = links.size();
        int i = 0;
        for( Element link : links ) {
            if( skipN > 0 ) {
                skipN--;
                continue;
            }
            else {
                i++;
                //                System.out.println(link);
            }

            String page = "";

            //if( i == 72 || i == 81) { //原url缺失
            //            if( i == 21){
            //            int temp = chapNums.indexOf( i );
            //                System.out.println("第" + i + "章 " ); //print title
            //
            //                page = trim( page, "", "（净）",
            //                    false,false );
            //                page = trim( page, "", "(净)",
            //                    false,false );
            //                page =  page + "\n"
            //                    + getContentFromFile( chapFiles.get( temp ) , i );
            //                //                System.out.println( page );
            //                page ="\n\n第" + i + "章 "+ page + "\n\n\n\n";
            //                //            System.out.println( page );
            //
            //                appendFile( file, page );
            //                i++;
            //                num++;
            //            }

            int temp = chapNums.indexOf( i );
            if( !chapNums.isEmpty() && temp != -1 ) {
                System.out.println( "第" + i + "章 " ); //print title

                page = trim( page, "", "（净）", false, false );
                page = trim( page, "", "(净)", false, false );
                page = page + "\n" + getContentFromFile( chapFiles.get( temp ),
                    i );
                //                System.out.println( page );
                page = "第" + i + "章 " + page + "\n\n\n\n";
            }
            else {
                page = link.text();
                if( page.charAt( 0 ) == '第' ) {
                    page = page.replaceAll( "第", "" ).replaceAll( "章", "" )
                        .replaceAll( " ", "" );
                }
                System.out.println( "第" + i + "章 " + page ); //print title
                String l = link.attr( "href" );
                if( l.charAt( 0 ) == '/' ) {
                    l = baseUrl + l;
                }
                page = page + getChapterContentFromUrlCss( l, i )
                    .replaceFirst( "第", "" );

            }

            page = "第" + i + "章 " + page + "\n\n\n\n";

            //            System.out.println( page );

            appendFile( file, page );
            printPercentage( i, num );
            //          break;
        }
    }


    public static Elements getChapterUrlListCss() {
        //        String url, String cssSelector ) {
        Document doc;

        try {
            //            System.out.println(sourceUrl);
            doc = Jsoup.connect( sourceUrl ).timeout( 999999 ).userAgent(
                "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 5.0)" ).get();
            String title = doc.title();
            Elements links = doc.select( listCssQuery );

            //            System.out.println(links);
            return links;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getChapterContentFromUrlCss( String url,
        int chapterIndex ) {

        Document doc;

        try {
            doc = Jsoup.connect( url ).timeout( 999999 ).userAgent(
                "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 5.0)" ).get();
            //            String title = "第" + chapterIndex + "章 " + trim( doc.title(),
            //                    titleKeepLast, titleKeepFront,
            //                    false, false ) + "\n";
            //


            Elements e = doc.select( contentCssQuery );
            //                        System.out.println( e );
            //
            //                        for( Element el:e.get(0).getAllElements()
            //                            .get( 0 ).getAllElements().get( 0 ).getAllElements()) {
            //                            System.out.println( el);
            //                            System.out.println( "--------------------\n\n\n");
            //                        }

            String content = "";//e.get( 0 ).toString();//.ownText();
            //            System.out.println( content );

            for( Element p : e ) {//TODO
                //                System.out.println( p.text() );
                content = content + p.text();
            }

            //            content = content.replaceAll( "　", "\n" );
            //            content = content.replaceAll( " ", "\n" );
            //            while( content.contains( "\n\n" ) ) {
            //                content = content.replaceAll( "\n\n", "\n" );
            //            }
            //            content = content.replaceAll( "\n", "\n    " );


            if( content.indexOf( "&nbsp;" ) != 0 ) {
                content = trim( e.html(), "&nbsp;", "", false, false );
            }


            content = content.replaceAll( "&nbsp;", " " )
                .replaceAll( "<br>", "" );
            //            System.out.println(content);

            for( Comment c : e.comments() ) {
                content = content.replaceAll( c.toString(), "" );
            }

            //                        System.out.println( content);

            return content;
        } catch ( IOException e ) {
            System.err.println( url );
            e.printStackTrace();
        }

        return null;

    }


    public static String getContentFromFile( File srcFile, int chapterIndex ) {

        Document doc;
        System.out.println( chapterIndex );

        try {

            doc = Jsoup.parse( srcFile, "UTF-8", "" );

            Elements e = doc.select( "div.read-txt p" );


            String content = e.text();
            content = content.replaceAll( "　", "\n" );
            content = content.replaceAll( " ", "\n" );
            while( content.contains( "\n\n" ) ) {
                content = content.replaceAll( "\n\n", "\n" );
            }
            content = content.replaceAll( "\n", "\n    " );
            //            System.out.println( content );
            //            appendFile( new File( chapterIndex + ".txt" ),
            //                "第" + chapterIndex + "章\n" + content );


            return content;
        } catch ( IOException e ) {
            System.err.println( file.getName() );
            e.printStackTrace();
        }

        return null;

    }


    public static Elements getChapterUrlList( String url, String listDivID ) {
        Document doc;

        try {
            doc = Jsoup.connect( url ).get();
            String title = doc.title();
            Element e = doc.getElementById( listDivID );
            Elements links = e.getElementsByTag( "a" );
            return links;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getChapterContentFromUrl( String url,
        String contentDivID, int chapterIndex ) {

        Document doc;

        try {
            doc = Jsoup.connect( url ).timeout( 999999 ).userAgent(
                "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 5.0)" ).get();
            String title = "";
            //                "第" + chapterIndex + "章 " + trim( doc.title(),
            //                    titleKeepLast, titleKeepFront,
            //                    false, false ) + "\n";

            System.out.println( title );


            Element e = doc.getElementById( contentDivID );


            String content = e.html();

            if( content.indexOf( "&nbsp;" ) != 0 ) {
                content = trim( e.html(), "&nbsp;", "", false, false );
            }


            content = content.replaceAll( "&nbsp;", " " )
                .replaceAll( "<br>", "" ).replaceAll( "<!--go-->", "" )
                .replaceAll( "<!--over-->", "" );
            //            System.out.println(content);


            content = title + "\n" + content + "\n\n\n";

            return content;
        } catch ( IOException e ) {
            System.err.println( url );
            e.printStackTrace();
        }

        return null;

    }


    public static void appendFile( File file, String content ) {
        try {

            if( !file.exists() ) {
                file.createNewFile();
            }

            //使用true，即进行append file
            FileWriter fileWritter = new FileWriter( file.getName(), true );

            fileWritter.write( content );
            //System.out.println( content );

            fileWritter.close();

        } catch ( IOException e ) {
            e.printStackTrace();

        }

    }



    public static void printPercentage( int n, int d ) {
        System.out.printf( "%.3f", (float) n * 100 / d );
        System.out.println( "% downloaded...\n" );
    }


    public static String trim( String original, String keepLast,
        String keepFirst, boolean frontInclude, boolean endInclude ) {

        if( keepFirst.length() != 0 && original.contains( keepFirst ) ) {
            if( endInclude ) {
                original = original
                    .substring( 0, original.lastIndexOf( keepFirst ) );
            }
            else {
                original = original
                    .substring( 0, original.lastIndexOf( keepFirst ) - 1 );

            }
        }
        if( keepLast.length() != 0 && original.contains( keepLast ) ) {
            if( frontInclude ) {
                original = original.substring( original.indexOf( keepLast ) );
            }
            else {
                original = original.substring(
                    original.indexOf( keepLast ) + keepLast.length() );
            }
        }
        return original;
    }

}

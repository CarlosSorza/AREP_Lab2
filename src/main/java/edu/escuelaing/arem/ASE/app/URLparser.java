
package edu.escuelaing.arem.ASE.app;
import java.net.MalformedURLException;
import java.net.URL;

public class URLparser 
{
    public static void main( String[] args ) throws MalformedURLException
    {
        URL sitionew = new URL ("https://campusvirtual.escuelaing.edu.co:5657/moodle/pluginfile.php/222974/mod_resource/content/0/NamesNetworkClientService.pdf");
        System.out.println( "Protocol " + sitionew.getProtocol() );
        System.out.println( "Authority " + sitionew.getAuthority() );
        System.out.println( "Host " + sitionew.getHost());
        System.out.println( "Port " + sitionew.getPort());
        System.out.println( "Path " + sitionew.getPath());
        System.out.println( "Query " + sitionew.getQuery());
        System.out.println( "File " + sitionew.getFile());
        System.out.println( "Ref " + sitionew.getRef());

    }
}

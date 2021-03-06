package caJava.core.wrapper;

import caJava.customOID.CustomBCStyle;
import caJava.fileManagement.Json;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNumericString;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.jcajce.provider.symmetric.DES;
import org.bouncycastle.util.Arrays;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/*хз зачем*/
public class SubjectMap {


/*    public static void main(String[] args) throws IOException {
        X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
        x500NameBld.addRDN(CustomBCStyle.СНИЛС, new DERNumericString("0012345678"));
        x500NameBld.addRDN(CustomBCStyle.ОГРН, new DERNumericString("0012345678"));
        x500NameBld.addRDN(CustomBCStyle.ИНН, new DERNumericString("0012345678"));
        x500NameBld.addRDN(BCStyle.STREET, "улица Улица, дом 84");
        x500NameBld.addRDN(BCStyle.EmailAddress, "mail@test.ru");
        x500NameBld.addRDN(BCStyle.C, "RU");
        x500NameBld.addRDN(BCStyle.ST, "77 Москва");
        x500NameBld.addRDN(BCStyle.L, ("г. Москва"));
        x500NameBld.addRDN(BCStyle.O, "НИИ \"Крекер\"");
        x500NameBld.addRDN(BCStyle.T, "Начальник отдела");
        x500NameBld.addRDN(BCStyle.GIVENNAME, "Иван Иванович");
        x500NameBld.addRDN(BCStyle.SURNAME, "Иванов");
        x500NameBld.addRDN(BCStyle.CN, "НИИ \"Крекер\"");
        File file = new File("subject.json");
        SubjectMap.save(x500NameBld.build(), file);
        X500Name name = SubjectMap.load(file);
        System.out.println(name);
    }*/


    public static X500Name load(File file) throws IOException {
        X500NameBuilder x500NameBld = new X500NameBuilder(CustomBCStyle.INSTANCE);
        Map<String, String> map = (Map<String, String>) Json.readValue(LinkedHashMap.class, file);
        map.forEach((String oid, String value) -> {
            x500NameBld.addRDN((ASN1ObjectIdentifier) CustomBCStyle.DefaultLookUp.get(oid.toLowerCase()), value);
        });
        return x500NameBld.build();
    }
    public static X500Name convert(Map<String, String> x500) {
        X500NameBuilder x500NameBld = new X500NameBuilder(CustomBCStyle.INSTANCE);
        x500.forEach((String oid, String value) -> {
            x500NameBld.addRDN((ASN1ObjectIdentifier) CustomBCStyle.DefaultLookUp.get(oid.toLowerCase()), value);
        });
        return x500NameBld.build();
    }
    public static Map<String, String> convert(X500Name x500Name) {
        Map<String, String> map = new LinkedHashMap<>();
        RDN[] rdNs = x500Name.getRDNs();
 /*       RDN[] rdNsReverse = new RDN[rdNs.length];
        for (int i = 0; i < rdNs.length; i++) {
            rdNsReverse[i]=rdNs[rdNs.length-i-1];
        }*/

        for (RDN rdN : rdNs) {
            //System.out.print(rdN.getFirst().getType()+"  ");
            //System.out.println(rdN.getFirst().getValue());
            map.put(CustomBCStyle.DefaultSymbols.get(new ASN1ObjectIdentifier(rdN.getFirst().getType().toString()).intern()).toString(), rdN.getFirst().getValue().toString());
        }
        return map;
    }

    public static void save(X500Name x500Name, File file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        RDN[] rdNs = x500Name.getRDNs();
        for (RDN rdN : rdNs) {
            //System.out.print(rdN.getFirst().getType()+"  ");
            //System.out.println(rdN.getFirst().getValue());
            map.put(CustomBCStyle.DefaultSymbols.get(new ASN1ObjectIdentifier(rdN.getFirst().getType().toString()).intern()).toString(), rdN.getFirst().getValue().toString());
        }
        //System.out.println(map);
        Json.write(map, file);
    }
}

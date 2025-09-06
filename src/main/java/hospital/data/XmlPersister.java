/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlPersister {
    private String path;
    private static XmlPersister theInstance;

    public static XmlPersister instance(){
        if (theInstance == null) theInstance = new XmlPersister("hospital.xml");
        return theInstance;
    }

    public XmlPersister(String p) {
        path = p;
    }

    public Data load() throws Exception{
        File file = new File(path);

        if (!file.exists()) {
            throw new Exception("se crea un archivo nuevo");
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        FileInputStream is = new FileInputStream(path);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Data result = (Data) unmarshaller.unmarshal(is);
        is.close();

        System.out.println("Datos cargados desde: " + file.getAbsolutePath());
        return result;
    }

    public void store(Data d) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        FileOutputStream os = new FileOutputStream(path);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        marshaller.marshal(d, os);
        os.flush();
        os.close();

        File file = new File(path);
        System.out.println("Datos guardados en: " + file.getAbsolutePath());
    }
}
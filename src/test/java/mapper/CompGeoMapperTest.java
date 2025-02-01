package mapper;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.mappers.CompGeoMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

class CompGeoMapperTest {

    ComprensorioGeograficoModel model;

    @BeforeEach
    void prepareTests() {
        CompGeoMapper mapper = new CompGeoMapper("comprensoriGeografici.json",
                new SerializerJSON<>());
        model = new ComprensorioGeograficoModel(mapper);

    }


    @Test
    void write() {
        ComprensorioGeografico tempComp1 =
                new ComprensorioGeografico("Brescia",
                        Arrays.asList("Brescia",
                                "Alfianello",
                                "Berlingo",
                                "Chiari",
                                "Sirmione"));
        ComprensorioGeografico tempComp2 =
                new ComprensorioGeografico("Bergamo",
                        Arrays.asList("Bergamo",
                                "Albino",
                                "Carona",
                                "Cassiglio",
                                "Membro",
                                "Zorro"));
        model.addComprensorio(tempComp1);
        model.addComprensorio(tempComp2);

    }

    @Test
    void read() {
        assert !model.getListaNomiComprensoriGeografici().isEmpty();
        System.out.println(model.getListaNomiComprensoriGeografici());
    }

}

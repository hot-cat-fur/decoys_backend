package pesko.orgasms.serviceUtils;

import pesko.orgasms.app.domain.entities.Orgasm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrgasmServiceUtil {


    public static List<Orgasm> createOrgasm(int count){


        return IntStream.range(0,count)
                .mapToObj(e->new Orgasm()).collect(Collectors.toList());
    }
}

package pesko.orgasms.serviceUtils;

import pesko.orgasms.app.domain.entities.User;



import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserServiceUtil {



    public static List<User>getUsers(int count){

       return IntStream.range(0,count)
                .mapToObj(e->new User())
                .collect(Collectors.toList());
    }
}

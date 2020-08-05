package pesko.orgasms.app.domain.entities;

public enum Roles {

    ROLE_GUEST(1),ROLE_USER(2),ROLE_ADMIN(3),ROLE_ROOT(4);

   private final int number;

    Roles(int number){
        this.number=number;
    }

    public int getNumber(){
        return this.number;
    }
}


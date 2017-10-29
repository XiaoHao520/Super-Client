package test;

/**
 * Created by xiaohao on 17-10-24.
 */

class ISayHello{
    public ISayHello() {
    }

    public void sayHello(SayHello sayHello){
        sayHello.sayHello();
    }
}

public class Test{
   public static void main(String agrs[]){
       ISayHello iSayHello=new ISayHello();
       iSayHello.sayHello(new SayHello() {
           @Override
           public void sayHello() {
               System.out.println("hello");
           }
       });
   }
}

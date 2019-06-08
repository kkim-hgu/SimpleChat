//https://github.com/korem32/SimpleChat

import java.net.*; // 네크워크 관련된 기능을 제공, 각종 프로토콜(http, ftp, telnet) 을 제공
import java.io.*; // 입/출력관련하여 각종 기능을 제공 [.* 를 붙인 이유는 io 와 관련된 모든 패키지를 사용하겠다는 뜻이다. ]
import java.util.Scanner; // 키보드로부터 데이타를 받겠다할때 사용하는 패키지
import java.text.SimpleDateFormat; //날짜를 형식으로 출력해줄때 사용하는 패키지
import java.util.Date; // 날짜를 출력을 위해 사용하는 패키지2


public class ChatClient { //여기서 public 은 접근제어자이다. Private -> protected ->public 순으로 default는 클래스내부나 동일 패키지 내에서 손 쉬운 접근이가능하다.
    
    public static void main(String[] args) { //static은 정적이라는 뜻이다, static으로 함수 또는 클래스를 선언했을 경우에는 자바가 컴파일 되는 순간 정의가 됩니다. 따라서 static 내에서 static 이 아닌 객체를 호출 하는 것은 불가능합니다. 그 이유는 static 이 먼저 선언되었지만, 다른 것들은 선언되지 않았기 때문입니다.
        // void 는 말그대로, 아무것도 없는, 즉 return 해주는 값이 따로 존재 하지 않는다는 뜻입니다.
        //    (String [] args) 는 String 문자열로 [] 배열을 선언해준 것 입니다. 즉 args[0], args[1] ‘’’’’ 에 원하는 값을 넣을 수 도 있습니다.
        //     하지만 위에서 말한 것처럼 사용하는 경우는 드물고, command line 에서 배열에 argument를 넘겨받을 때 사용합니다.
        //   바로 밑에, if(args.length !=2)가 아닐 경우에는, System.exit(1) 시스템을 종료한다고 나와있습니다.
        if(args.length !=2 ){ //    즉 args[0] = “첫번째 인자”, args[1] = “두번째 인자” 까지 받지 않으면 그것은 프로그램을 종료하겠다는 뜻입니다!
            System.out.println("Usage : java Chatclient <username> <server-ip>" );
            System.exit(1);
        }
        
        boolean isbadWord = false;
        Socket sock = null; // java.net 패키지 안에 있는 Socket class와 같은 구조를 띄게끔 를 sock이라 명칭하여 null로서 객체를 선언해준 것이다.
        BufferedReader br = null; //java.io 패키지 안에 있는 BufferedReader class와 같은 구조를 띄게끔 를 br이라 명칭하여 null로서 객체를 선언해준 것이다.
        PrintWriter pw = null; //java.io 패키지 안에 있는 PrintWriter class와 같은 구조를 띄게끔 를 pw이라 명칭하여 null로서 객체를 선언해준 것이다.
        boolean endflag = false; //ndflag 를 Boolean 타입으로 선언하고 false 라 칭한다.
        String [] badWord = new String [100]; // badword 스트링 크기 100인 어레이를 선언해준다.
        Scanner scan = new Scanner(System.in); // Scanner 로서 사용할 변수의 이름은 scan
        
        //String ip = scan.nextLine(); // ip를 string type으로 선언하고, 그곳에 scan 을 통해서 키보드로 부터 입력을 받는다.
        
        
        try{ //    Try 구문은 밑에 catch 구문까지 명령들을 실행하고 오류가 있을 시, catch구문을 통하여 잡아낼 수 있다.
            sock = new Socket(args[1], 10001); //   sock 에 commnadline에서 받은 args[1]과 10001을 객체를 생성할 때 인자로서 전달한다는 뜻입니다.
            pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream())); //   pw 에는 OutputStreamWriter라는 메소드에 sock.getOutputstream()의 메소드에서 반환된 값을 PrintWriter로 객체 생성할때 전달합니다.
            br = new BufferedReader(new InputStreamReader(sock.getInputStream())); //   br 에는 InputStreamReader라는 메소드에 sock.getInputStream()의 메소드에서 반환된 값을 BufferReader로 객체 생성할때 전달합니다.
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));//    BufferReader와 같은 구조를 띄는 keyboard 를 선언해줌과 동시에 System.in 을 통하여 실제 키보드를 통하여 user의 input을 받는다.
            // send username.
            pw.println(args[0]); //prinln이라는 메소드안에 args[0]을 인자로서 전달해주고, 실행
            pw.flush(); //flush()라는 메소드를 실행한다. ()인 이유는 전달해줄 인자가 필요하지 않다.
            
            
            InputThread it = new InputThread(sock, br); //InputThread 라는 클라스 구조를 띄는 it의 이름을 가진 객체를 선언하여 줄때 sock, br 객체를 인자로서 전달하여 준다.
            it.start(); //it이라는 객체의 start() 메소드를 실행한다. (전달받을 인자는 따로 없다)
            String line = null; //String type의 line이라는 이름을 가진 변수를 선언하여 줄때, null로 선언하여준다.
            while((line = keyboard.readLine()) != null){ //while 반복구문을 실행한다, 실행조건은 이전에 선언한 String type의 line의 변수에 keyboard.readLine 메소드를 이용하여 키보드로 부터 한 줄을 입력 받은 값을 저장하고, 그것이 null이 아니면 실행한다.
                
                int length = badWord.length; // badWord 어레이의 길이를 잰다.
                int i=0; // int type i 를 변수로 선언하고 0으로 초기화해준다.
                if(line.equalsIgnoreCase("asshole")|| line.equalsIgnoreCase("bitch") || line.equalsIgnoreCase("shutup")||line.equalsIgnoreCase("fuck")||line.equalsIgnoreCase("dick")){ //string line에 저장되어있는 값이 "asshole"이라는 스트링과 대소문자 구분하지 않고 똑같다면 아래의 명령을 실행해준다.
                    System.out.println("Stop using bad words"); //출력한다.
                    
                }
                else{
                    pw.println(line); //그리고 앞서 입력받은 String type의 line에 저장되어 있는 값을 pw 객체에 있는 println 메소드에 인자로서 전달한다.
                    pw.flush(); //pw 객체의 flush()라는 메소드를 실행하여 준다.
                    
                    if(line.equals("/spamlist")){ //만약 String type의 line에 저장되어있는 값이 “/spamlist”과 대소문자까지 맞다면 if 구문을 실행한다.
                        System.out.println("Bad words list: "); //출력한다.
                        while(i < length){ //i가 lengh 보다 작으면 아래의 while 구문 실행~
                            System.out.println(badWord[i]); //badWord어레이의 i번째를 출력한다.
                            i++; // i의 값이 1씩 커진다.
                            if(badWord[i] == null){ //만약 badWord 러에의 i 번째의 있는 값이 null인 경우 아래의 구문을 실행한다.
                                break; //while 구문을 탈출한다.
                            } //if
                        }//while
                        
                    }//if
                    
                    
                    
                    if(line.equals("/addspam")){ //strying line에 저장되어있는 값이 /addspam과 같다면 아래를 실행
                        
                        
                        System.out.println("Input the badword that you want to add: "); //출력한다.
                        badWord[i] = scan.next(); //badWord i 번째 어레이를 키보드로 부터 받는다.
                        i++; //i를 1씩 증가시킨다.
                        
                    }
                    
                    if(line.equals("/quit")){ //strying line에 저장되어있는 값이 /quit과 같다면 if 구문 아래를 실행하다.
                        endflag = true; //Boolean type의 endflog를 true을 저장한다.
                        break; //while 구문을 탈출해줍니다.
                    }
                    
                }
            }
            System.out.println("Connection closed."); //출력한다.
        }catch(Exception ex){ //오류 발생시 ex로 전달
            if(!endflag) //endflag 가 false 일 경우 실행
                System.out.println(ex); //ex를 출력한다/
        }finally{
            try{ // 아래를 시도해본다.
                if(pw != null) // pw가 null이 아닌경우
                    pw.close(); //pw 클래스내의 close 메소드를 실행한다.
            }catch(Exception ex){}
            try{
                if(br != null) //br 이 null 이 아닌 경우
                    br.close(); // br클래스 내의 close 메소드를 실행한다.
            }catch(Exception ex){}
            try{
                if(sock != null) //sock이 null이 아닌 경우
                    sock.close(); //sock 클래스내의 close 메소드를 실행한다.
            }catch(Exception ex){}
        } // finally
    } // main
    
    
} // class

class InputThread extends Thread{ //Thread를 상속하고 있는 InputThread클래스를 선언한다.
    private Socket sock = null; //private type의 Socket 클래스를 sock 이라는 이름으로 객체선언하고, null 값으로 초기화한다.
    private BufferedReader br = null; //private type의 BufferedReader 라는 이름으로 br을 객체선언하고, null 값을 초기화 한다.
    public InputThread(Socket sock, BufferedReader br){ //InputThread의 생성자를 만들고, 인자를 받아와서 사용할 수 있다.
        this.sock = sock; // sock과 br을 전달받고 그것을 InputThread의 private 타입으로 사용한다.
        this.br = br; // sock과 br을 전달받고 그것을 InputThread의 private 타입으로 사용한다.
    }
    public void run(){ // InputThread 클래스의 run 함수를 실행한다.
        try{ //밑에 명령어들의 오류가 있는 판단하고 있으면 catch 구문을 실행한다.
            String line = null; //String type으로 line이라는 이름을 가진 변수선언 해준다.
            while((line = br.readLine()) != null){ //line = br.readLine()메소드를 통해서 값을 읽고 null이 아닌 경우 while 구문을 실행한다.
                Date date = new Date(); // 발생될때마다 만들어주기 위함
                SimpleDateFormat test = new SimpleDateFormat("[hh:mm:ss]");// SimpleDateFormat 와 같은 클래스를 가진 test로 객체선언 함과 동시에 "[hh:mm:ss]"의 인자를 넣어준다.
                System.out.println(test.format(date)); //test.format()메소드에 date를 넣고 출력한다.
                System.out.println(line); //line에 저장된 값을 출력한다.
            }
        }catch(Exception ex){ // 예외 발생시 처리해준다.
        }finally{ //예외 발생하든 안하든 항상 실행되는 것 예를들면, 데이타베이스 오류발생시 창을 닫아야 하는 것 처럼
            try{ // 아래의 구문을 try 해본다.
                if(br != null) //br 의 저장되어있는 값이 null 이 아닌 경우, 아래 명렁어를 실행한다.
                    br.close(); // br클래스내에 close() 메소를들 실행한다.
            }catch(Exception ex){} //예외 발생시 아래의 구문을 실행
            try{ //아래 구문을 실행하여 준다.
                if(sock != null) //sock에 저장되어 있는 값이 null이 아닌 경우, sock.close() 닫아준다.
                    sock.close();
            }catch(Exception ex){} //예외 발생시 처리해준다.
        } //finally
    } // InputThread
}




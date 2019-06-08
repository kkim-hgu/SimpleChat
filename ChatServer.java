//https://github.com/korem32/SimpleChat

import java.net.*; // 네크워크 관련된 기능을 제공, 각종 프로토콜(http, ftp, telnet) 을 제공
import java.io.*; // 입/출력관련하여 각종 기능을 제공 [.* 를 붙인 이유는 io 와 관련된 모든 패키지를 사용하겠다는 뜻이다. ]
import java.util.*; // util. 관련 패키지 모두 선언
import java.text.SimpleDateFormat; //날짜를 형식으로 출력해줄때 사용하는 패키지




public class ChatServer {//여기서 public 은 접근제어자이다. Private -> protected ->public 순으로 default는 클래스내부나 동일 패키지 내에서 손 쉬운 접근이가능하다.
    
    public static void main(String[] args) {
        //static은 정적이라는 뜻이다, static으로 함수 또는 클래스를 선언했을 경우에는 자바가 컴파일 되는 순간 정의가 됩니다. 따라서 static 내에서 static 이 아닌 객체를 호출 하는 것은 불가능합니다. 그 이유는 static 이 먼저 선언되었지만, 다른 것들은 선언되지 않았기 때문입니다.
        // void 는 말그대로, 아무것도 없는, 즉 return 해주는 값이 따로 존재 하지 않는다는 뜻입니다.
        //    (String [] args) 는 String 문자열로 [] 배열을 선언해준 것 입니다. 즉 args[0], args[1] ‘’’’’ 에 원하는 값을 넣을 수 도 있습니다.
        //     하지만 위에서 말한 것처럼 사용하는 경우는 드물고, command line 에서 배열에 argument를 넘겨받을 때 사용합니다.
        try{ //try 구문을 통해서 예외를 확인한다.
            ServerSocket server = new ServerSocket(10001); //server 라는 이름의 객체를 ServerSocket의 클래스로 선언해주며, 동시에 10001의 값을 인자로서 전달.
            System.out.println("Waiting connection..."); //출력한다.
            HashMap hm = new HashMap(); //HashMap 클래스와 같은 구조를 가지게 hm을 이름으로서 선언해준다, 전달되는 인자는 없다.
            while(true){ // while구문을 계속 반복하여 실행한다.
                Socket sock = server.accept(); // Socket 클래스 구조를 가진 sock을 이름으로서 선언해줌과 동시에 server.accept()메소드를 실행해서 return 되는 값을 저장해준다.
                ChatThread chatthread = new ChatThread(sock, hm); // ChatTread 클래스로 chatthread 라는 이름의 객체를 선언해줌과 동시에 생성자를 통하여 sock, hm을 인자로서 전달한다.
                chatthread.start(); //chatthread 클래스의 strat 메소드를 실행한다.
            } // while
        }catch(Exception e){ //예외 발생시
            System.out.println(e); //에외 발생 했다는 구문 출력
        } //catch
    } // main
} //ChatServer

class ChatThread extends Thread{ //ChatThread 는 Thread를 상속한다.
    private Socket sock; //private type으로 Socket의 타입을 띄는 sock를 선언해준다,
    private String id; // //private type으로 String의 타입을 띄는 id를 선언해준다,
    private BufferedReader br; // //private type으로 BufferedREader의 구조를 띄는 br를 선언해준다,
    private HashMap hm; ////private type으로 HashMap의 구조를 띄는 hm를 선언해준다,
    private boolean initFlag = false; ////private type으로 boolean 구조를 띄는 iniFlag를 선언함과 동시에, false로 초기화 해준다.
    private String [] idList = new String [5]; ////private String type을 띄는 idList 어레이를 5개 선언해준다.
    private int count = 0; // //private int type을 띄는 count 변수를 선언과 동시에 0으로 초기화
    private int temp1, temp2;
    public ChatThread(Socket sock, HashMap hm){ //ChatThread 생성자를 통하여서 sock 과 hm을 인자로서 전달받고
        this.sock = sock; // Private 으로 저장되어있는 값에, 저장해준다.
        this.hm = hm; // Private 으로 저장되어있는 값에, 저장해준다.
        try{ //아래 구문을 예외가 발생하는 지 실행한다.
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream())); //PrintWriter type의 pw를 선언해줌과 동시에 sock.getOutputStream()메소드를 통해 return된 값을 OuputSteramWriter에게 전달하고 return 된 값을 생성자 PriWriter에 전달해준다.
            br = new BufferedReader(new InputStreamReader(sock.getInputStream())); //br에 생성자 BufferedReader에 sock.getInputStream()메소드를 통해서 return 된 값을 InputStreamReader 생성자에 전달하고, return 된 값을 BufferedRead에 생성자로서 전달한다.
            id = br.readLine(); //id에 br.readLine()메소드를 실행시키고, 그것 통해 return된 값을 저장해준다.
            
            Date date = new Date(); // 발생될때마다 만들어주기 위함
            SimpleDateFormat test = new SimpleDateFormat("[hh:mm:ss]"); //SimpleDateFormat 와 같은 클래스를 가진 test로 객체선언 함과 동시에 "[hh:mm:ss]"의 인자를 넣어준다.
            //System.out.println(test.format(date)); //test.format()메소드에 date를 넣고 출력한다.
            
            
            broadcast(id + " entered."); //broadcast를 통해 id에 저장된 값과 entered. 문장을 출력한다.
            
            //count가 5보다 작을때 실행한다.
            idList[count] = id; //idList[count]에 id의 값을 저장해준다.
            count++; //count를 1씩 증가시킨다.
            
            // count을 0으로 초기화시킨다.
            System.out.println("[Server] User (" + id + ") entered."); // 출력한다.
            synchronized(hm){ //Thread가 연속으로 생성되는 것을 방지하나, 오직 한개의 쓰레드만 접근할 수 있도록 하여준다.
                hm.put(this.id, pw); //hm. put() 메소드에 this.id와 pw를 전달하여 실행한다.
            }
            initFlag = true; //initFlag을 true로 저장하여 준다.
        }catch(Exception ex){ //예외 발생시 아래 구문으로 처리해준다.
            System.out.println(ex); //출력
        }
    } // construcor
    public void run(){ //run 메소드 이다.
        try{ //예외 발생하는지 아래 구문들을 실행한다.
            String line = null; //string type으로 lined이라는 이름으로 변수를 선언함과 동시에 null로 초기화한다.
            while((line = br.readLine()) != null){ //br.readLine()메소드를 통해서 return된 값을 line에 저장함과 동시에 null이 아닌경우, while 구문을 실행한다.
                if(line.equals("/quit")) //line에 저장되어있는 값이 /quit 과 같은경우 break; 를 이용하여 while 구문을 탈출한다.
                    break;
                
                if(line.equals("/userlist")){ //line에 저장되어 있는 값이 /idlist와 같은 경우 if 구문을 실행한다.
                    
                    _senduserlist(); //총 인원서버에 출력!
                    
                }
                
                if(line.indexOf("/to ") == 0){ //line에 저장되어있는 값이 /to와 같을 경우 sendmsg 메소드에 line을 넗고 실행하여 준다.
                    sendmsg(line);
                    
                }else // if 구문에 조건에 맞지 않은 경우 이 else 구문을 실행한다.
                    broadcastNormal(id + " : " + line); //broadcast 를 이용해 출력하여준다.
            }
        }catch(Exception ex){ //예외 발생시 아래 문장 출력
            System.out.println(ex);
        }finally{ //예외 발생하든지 않하든지 항상 실행.
            synchronized(hm){ //thread는 하나씩만 접근가능
                hm.remove(id); //hm.remove 를 통하여서 id를 삭제해줌
            }
            
            Date date = new Date(); // Date 를 사용하여, date객체를 선언하여준다.
            SimpleDateFormat test = new SimpleDateFormat("[hh:mm:ss]"); //날짜 형식을 지정하여 준다.
            System.out.println(test.format(date)); // 출력하여준다.
            
            System.out.println("[Server] User (" + id + ") exit."); //출력
            
            broadcast(id + " exited."); //출력
            
            try{ // 아래 구문을 실행한다.
                if(sock != null) //sock 이 null로 저장되어 있을 경우
                    sock.close(); //sock.close() 메소드를 실행.
            }catch(Exception ex){} //예외 발생시 구문 실행
        }
    } // run
    public void sendmsg(String msg){ //sendmsg 메소드에 대한 내용입니다.
        int start = msg.indexOf(" ") +1; //start 변수를 = msg.indexof()메소드에서 나온 return 값과 +1를 를 한 값을 저장합니다.
        int end = msg.indexOf(" ", start); //end 변수에= msg.indexof()메소드에서 나온 return 값과
        if(end != -1){ //end 가 -1 일과 같지 않을 경우 if 구문을 실행한다.
            String to = msg.substring(start, end); // strying type 변수 to 에 msg.substring메소드에 start, end 값을 넣고 return 된 값을 저장한다.
            String msg2 = msg.substring(end+1); //strying type 변수 to 에 msg.substring메소드에 end +1 값을 넣고 return 된 값을 저장한다.
            Object obj = hm.get(to); //Object type의 객체를 선언하여 주고, hm.get() 메소드에 to 를 넣고 return 되는 값으로 저장해준다.
            if(obj != null){ //obj 가 null이 아닐경우
                PrintWriter pw = (PrintWriter)obj; // PrintWriter type의 pw를 선언해주고 obj 를 PrintWriter 으로 type casting 을 진행하여 주고
                pw.println(id + " whisphered. : " + msg2); //출력한다.
                pw.flush(); //pw.flush() 메소드를 사용한다.
            } // if
        }
    } // sendmsg
    
    
    public void broadcast(String msg){ //broadcast 메소드에 대한 정의이다.
        synchronized(hm){ //Thread 하나만 접근할 수 있도록 한다.
            Collection collection = hm.values(); //Collection type으로 collection 을 선언하여주고, 거기에 hm.values() 라는 메소드를 통해 return 된 값을 저장하여 준다.
            Iterator iter = collection.iterator(); //Iterator type으로 iter를 선언해주고 collection.iterator() 메소드를 통해 return 된 값을 저장해준다.
            
            
            while(iter.hasNext()){ // iter.hasNext() 나온 값이 true일 경우 계속 실행한다.
                PrintWriter pw = (PrintWriter)iter.next(); //PrintWriter type으로 pw을 선언하여주고, 거기에 iter.next()을 통해 나온 값을 type casting 을통해 저장하여 준다.
                pw.println(msg); //pw.println 메소드에 msg 를 인자로서 전달하고 실행한다.
                pw.flush(); //pw.flush()메소드를 실행한다.
            }
        }
    } // broadcast
    
    public void broadcastNormal(String msg){ //broadcast 메소드에 대한 정의이다.
        synchronized(hm){ //Thread 하나만 접근할 수 있도록 한다.
            Object obj = hm.get(id);
            Collection collection = hm.values(); //Collection type으로 collection 을 선언하여주고, 거기에 hm.values() 라는 메소드를 통해 return 된 값을 저장하여 준다.
            Iterator iter = collection.iterator();
            Set key = hm.keySet();
            Iterator itr = key.iterator();
            String keyname = null;
            
            
            while(iter.hasNext()){  //obj 가 null이 아닐경우
                PrintWriter pw = (PrintWriter)iter.next();
                // PrintWriter type의 pw를 선언해주고 obj 를 PrintWriter 으로 type casting 을 진행하여 주고
                keyname = (String)itr.next();
                if(keyname != id){
                    
                    pw.println(msg); //출력한다.
                    pw.flush();} //pw.flush() 메소드를 사용한다.
            } // if
        }
    } // broadcast
    
    public void _senduserlist(){ //서버에 총 인원 출력!
        String name;
        Iterator itr = hm.entrySet().iterator();
        Map.Entry<String, Object> e = (Map.Entry<String, Object>)itr.next();
        PrintWriter pw = (PrintWriter)e.getValue();
        
        while(itr.hasNext())
        {
            
            name = e.getKey();
            //pw.println(name);
            System.out.println(name);
            // pw.flush();
            e = (Map.Entry<String, Object>)itr.next();
            
        }
        
        name = e.getKey();
        // pw.println(name);
        System.out.println(name);
        //pw.flush();
        
    }
}


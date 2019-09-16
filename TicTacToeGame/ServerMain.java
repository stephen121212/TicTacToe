package javaapplication12;



//Source: https://www.youtube.com/playlist?list=PLlrATfBNZ98cCvNtS7x4u1bZOS5FBwOSb

//Class that is run to construct the server
public class ServerMain {

	private int port;
	private Server server;

	public ServerMain(int port) {
		this.port = port;
		server = new Server(port);
	}

	public static void main(String[] args) {
		int port = 1337;
		new ServerMain(port);
	}

}

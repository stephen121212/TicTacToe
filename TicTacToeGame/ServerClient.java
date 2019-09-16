package javaapplication12;



import java.net.InetAddress;

//Source: https://www.youtube.com/playlist?list=PLlrATfBNZ98cCvNtS7x4u1bZOS5FBwOSb

//class containing the details of the server
public class ServerClient {

	public String name;
	public InetAddress address;
	public int port;
	private final int ID;
	public int attempt = 0;

	public ServerClient(String name, InetAddress address, int port, final int ID) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}

}

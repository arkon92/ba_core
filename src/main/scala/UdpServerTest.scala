import java.io.{ByteArrayInputStream, File}
import java.net.{DatagramPacket, DatagramSocket}
import java.util.Calendar
import javax.imageio.ImageIO

/**
  * Created by romain on 14/02/2017.
  */
object UdpServerTest
{
  def main(args: Array[String]) {
    val IMAGE_LENGTH_SIZE = 8
    val PORT = 5005

    val socket = new DatagramSocket(PORT)

    println("Test socket is open")

    while(true)
    {
      val bufferForImageLength = new Array[Byte](IMAGE_LENGTH_SIZE)
      val packetWithImageLength = new DatagramPacket(bufferForImageLength, bufferForImageLength.length)
      socket.receive(packetWithImageLength)
      val imageLengthAsString = new String(packetWithImageLength.getData)
      try {
        val imageLength = imageLengthAsString.replaceAll("[^\\d]","").toInt
        val ipAddress = packetWithImageLength.getAddress().toString
        println(Calendar.getInstance.getTime + "received from " + ipAddress + ": " + imageLength.toString)

        val bufferForImage = new Array[Byte](imageLength)
        val packetWithImage = new DatagramPacket(bufferForImage, bufferForImage.length)
        socket.receive(packetWithImage)
        println(Calendar.getInstance.getTime + "received image from " + ipAddress )

        val image = ImageIO.read(new ByteArrayInputStream(packetWithImage.getData))

        val outputfile = new File("image.jpg")
        ImageIO.write(image, "jpg", outputfile)

      } catch {
        case e: Exception => println(e)
      }
    }
  }
}
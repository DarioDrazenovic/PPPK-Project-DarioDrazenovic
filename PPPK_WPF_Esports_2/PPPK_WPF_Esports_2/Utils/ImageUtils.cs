using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace PPPK_WPF_Esports_2.Utils
{
    public static class ImageUtils
    {
        public static BitmapImage ByteArrayToBitmapImage(byte[] picture)
        {
            using (var memoryStream = new MemoryStream(picture))
            {
                var bitmap = new BitmapImage();
                bitmap.BeginInit();
                bitmap.StreamSource = memoryStream;
                bitmap.CacheOption = BitmapCacheOption.OnLoad;
                bitmap.EndInit();
                bitmap.Freeze();
                return bitmap;
            }
        }
        public static byte[] BitmapImageToByteArray(BitmapImage image)
        {
            var jpegEncoder = new JpegBitmapEncoder();
            jpegEncoder.Frames.Add(BitmapFrame.Create(image));
            using (var memoryStream = new MemoryStream())
            {
                jpegEncoder.Save(memoryStream);
                return memoryStream.ToArray();
            }
        }

        public static byte[] ByteArrayFromSqlDataReader(SqlDataReader dr, int column)
        {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int currentBytes = 0;

            using (var memoryStream = new MemoryStream())
            {
                using (var binaryWriter = new BinaryWriter(memoryStream))
                {
                    int readBytes;
                    do
                    {
                        //procitaj bytes iz neke kolone, krecemo od nule, zagrabi 1024 komada
                        readBytes = (int)dr.GetBytes(column, currentBytes, buffer, 0, bufferSize);
                        //preko binaryWritera bacamo u memory stream
                        binaryWriter.Write(buffer, 0, readBytes);
                        //pomicemo currentBytes
                        currentBytes += readBytes;

                    } while (readBytes == bufferSize);

                    return memoryStream.ToArray();
                }
            }
        }
    }
}

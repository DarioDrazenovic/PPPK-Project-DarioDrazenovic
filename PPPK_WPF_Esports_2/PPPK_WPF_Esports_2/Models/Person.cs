using PPPK_WPF_Esports_2.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace PPPK_WPF_Esports_2.Models
{
    public class Person
    {
        public int IDPerson { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public int Age { get; set; }
        public string Email { get; set; }
        public byte[] Picture { get; set; }
        public BitmapImage Image { 
            get => ImageUtils.ByteArrayToBitmapImage(Picture); }

        public override string ToString()
        {
            return FirstName + " " + LastName;
        }
    }
}

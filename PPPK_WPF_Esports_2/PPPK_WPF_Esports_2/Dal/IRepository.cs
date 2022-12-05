using PPPK_WPF_Esports_2.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.Dal
{
    public interface IRepository
    {
        void AddPerson(Person person);
        void UpdatePerson(Person person);
        void DeletePerson(Person person);
        Person GetPerson(int idPerson);
        IList<Person> GetPeople();
    }
}

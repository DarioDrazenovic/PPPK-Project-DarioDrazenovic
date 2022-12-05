using PPPK_WPF_Esports_2.Models;
using PPPK_WPF_Esports_2.Utils;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.Dal
{
    class SqlRepository : IRepository
    {
        private static readonly string cs = 
            EncryptionUtils.Decrypt(
            ConfigurationManager.ConnectionStrings["cs"].ConnectionString, "Pa$$w0rd");

        private const string FirstNameParam = "@firstname";
        private const string LastNameParam = "@lastname";
        private const string AgeParam = "@age";
        private const string EmailParam = "@email";
        private const string PictureParam = "@picture";
        private const string IdPersonParam = "@idPerson";

        public void AddPerson(Person person)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                                    //sa MethodBase pitamo stack u kojoj se metodi nalazimo
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(FirstNameParam, person.FirstName);
                    cmd.Parameters.AddWithValue(LastNameParam, person.LastName);
                    cmd.Parameters.AddWithValue(AgeParam, person.Age);
                    cmd.Parameters.AddWithValue(EmailParam, person.Email);
                    cmd.Parameters.Add(
                        new SqlParameter(PictureParam, System.Data.SqlDbType.Binary, person.Picture.Length)
                        {
                            Value = person.Picture
                        });

                    SqlParameter id = new SqlParameter(IdPersonParam, System.Data.SqlDbType.Int)
                    {
                        Direction = System.Data.ParameterDirection.Output
                    };
                    cmd.Parameters.Add(id);
                    cmd.ExecuteNonQuery();
                    person.IDPerson = (int)id.Value;
                }
            }
        }

        public void DeletePerson(Person person)
        {
            IList<EsportsTeam> teams = RepositoryFactory.GetRepositoryEsports().GetEsportsTeams();

            foreach (var team in teams)
            {
                if (team.Player.FirstName.Equals(person.FirstName) && team.Player.LastName.Equals(person.LastName))
                {
                    RepositoryFactory.GetRepositoryEsports().DeleteEsportsTeam(team);

                }
            }

            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    //sa MethodBase pitamo stack u kojoj se metodi nalazimo
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(IdPersonParam, person.IDPerson);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public IList<Person> GetPeople()
        {
            IList<Person> people = new List<Person>();

            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    //sa MethodBase pitamo stack u kojoj se metodi nalazimo
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            people.Add(ReadPerson(dr));
                        }
                    }
                }
            }
            return people;
        }

        private Person ReadPerson(SqlDataReader dr)
            => new Person
            {
                IDPerson = (int)dr[nameof(Person.IDPerson)],
                FirstName = dr[nameof(Person.FirstName)].ToString(),
                LastName = dr[nameof(Person.LastName)].ToString(),
                Age = (int)dr[nameof(Person.Age)],
                Email = dr[nameof(Person.Email)].ToString(),
                Picture = ImageUtils.ByteArrayFromSqlDataReader(dr, 5)
            };

        public Person GetPerson(int idPerson)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    //sa MethodBase pitamo stack u kojoj se metodi nalazimo
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(IdPersonParam, idPerson);

                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        if (dr.Read())
                        {
                            return ReadPerson(dr);
                        }
                    }
                }
            }
            throw new Exception("Wrong id");
        }

        public void UpdatePerson(Person person)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    //sa MethodBase pitamo stack u kojoj se metodi nalazimo
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(FirstNameParam, person.FirstName);
                    cmd.Parameters.AddWithValue(LastNameParam, person.LastName);
                    cmd.Parameters.AddWithValue(AgeParam, person.Age);
                    cmd.Parameters.AddWithValue(EmailParam, person.Email);
                    cmd.Parameters.AddWithValue(IdPersonParam, person.IDPerson);
                    cmd.Parameters.Add(
                        new SqlParameter(PictureParam, System.Data.SqlDbType.Binary, person.Picture.Length)
                        {
                            Value = person.Picture
                        });

                    cmd.ExecuteNonQuery();
                }
            }
        }
    }
}

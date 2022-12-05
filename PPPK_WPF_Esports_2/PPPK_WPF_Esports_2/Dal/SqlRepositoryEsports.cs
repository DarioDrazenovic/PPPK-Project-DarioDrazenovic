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
    class SqlRepositoryEsports : IRepositoryEsports
    {
        private static readonly string cs = EncryptionUtils.Decrypt(ConfigurationManager.ConnectionStrings["cs"].ConnectionString, "Pa$$w0rd");

        private const string NameParameter = "@teamName";
        private const string CountryParameter = "@country";
        private const string PersonIdParameter = "@personid";
        private const string IdEsportsTeamParameter = "@idETeam";

        public void AddEsportsTeam(EsportsTeam team)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(NameParameter, team.TeamName);
                    cmd.Parameters.AddWithValue(CountryParameter, team.Country);
                    cmd.Parameters.AddWithValue(PersonIdParameter, team.Player.IDPerson);

                    SqlParameter id = new SqlParameter(IdEsportsTeamParameter, System.Data.SqlDbType.Int)
                    {
                        Direction = System.Data.ParameterDirection.Output
                    };

                    cmd.Parameters.Add(id);
                    cmd.ExecuteNonQuery();
                    team.IDETeam = (int)id.Value;
                }
            }
        }

        public void DeleteEsportsTeam(EsportsTeam team)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(IdEsportsTeamParameter, team.IDETeam);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public IList<EsportsTeam> GetEsportsTeams()
        {
            IList<EsportsTeam> team = new List<EsportsTeam>();
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            team.Add(ReadTeam(dr));
                        }
                    }
                }
            }
            return team;
        }

        private EsportsTeam ReadTeam(SqlDataReader dr)
        => new EsportsTeam
        {
            IDETeam = (int)dr[nameof(EsportsTeam.IDETeam)],
            TeamName = dr[nameof(EsportsTeam.TeamName)].ToString(),
            Country = dr[nameof(EsportsTeam.Country)].ToString(),
            Player = RepositoryFactory.GetRepository().GetPerson((int)dr["PersonID"])
        };

        public EsportsTeam GetEsportTeam(int idEsportsTeam)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(IdEsportsTeamParameter, idEsportsTeam);
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        if (dr.Read())
                        {
                            return ReadTeam(dr);
                        }
                    }
                }
            }
            throw new Exception("wrong id");
        }

        public void UpdateEsportsTeam(EsportsTeam team)
        {
            using (SqlConnection con = new SqlConnection(cs))
            {
                con.Open();
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = MethodBase.GetCurrentMethod().Name;
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue(NameParameter, team.TeamName);
                    cmd.Parameters.AddWithValue(CountryParameter, team.Country);
                    cmd.Parameters.AddWithValue(PersonIdParameter, team.Player.IDPerson);
                    cmd.Parameters.AddWithValue(IdEsportsTeamParameter, team.IDETeam);
                    cmd.ExecuteNonQuery();

                }
            }
        }
    }
}

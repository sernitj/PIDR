package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LesionDao;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;

public class LesionDaoImpl extends DaoAutoIncrementImpl implements LesionDao {
    private static Connection connection;

    public LesionDaoImpl(Connection connection) {
        LesionDaoImpl.connection = connection;
    }

    public static void delete(int id) {
        LesionDaoImpl.delete(SQLConnection.getConnection(), "lesion", id);
    }

    /*public static boolean moreThanOneDiag(Diag diag) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            ArrayList<String> diags = new ArrayList<>();
            statement = LesionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT DIAGNOSTIC FROM lesion");

            while (resultSet.next())
                diags.add(resultSet.getString("DIAGNOSTIC"));

            System.out.println("SELECT DIAGNOSTIC FROM lesion");

            if (Collections.frequency(diags, diag) > 1)
                return true;
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }*/

    public static ArrayList<Lesion> removeLesions(String id) {
        ArrayList<Lesion> lesions = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SQLConnection.getConnection().prepareStatement("SELECT ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, DIAGNOSTIC, FILE_DIAG FROM lesion WHERE ID_INCLUSION = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesion lesion = new Lesion();

                lesion.setId(resultSet.getInt("ID"));
                lesion.setIdInclusion(resultSet.getInt("ID_INCLUSION"));
                lesion.setPhotoSur(resultSet.getString("PHOTO_SUR"));
                lesion.setPhotoHors(resultSet.getString("PHOTO_HORS"));
                lesion.setPhotoFixe(resultSet.getString("PHOTO_FIXE"));
                lesion.setDiag(resultSet.getString("DIAGNOSTIC"));
                lesion.setFileDiag(resultSet.getString("FILE_DIAG"));

                lesions.add(lesion);
            }

            System.out.println("SELECT ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, DIAGNOSTIC, FILE_DIAG, FICHIER_MOY FROM lesion WHERE ID_INCLUSION = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lesions;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("INSERT INTO lesion (ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG, FICHIER_MOY)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?))");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Lesion selectById(int id) {
        Lesion lesion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("SELECT * FROM lesion WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                lesion = this.addToLesion(resultSet);

            System.out.println("SELECT * FROM lesion WHERE ID ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lesion;
    }

    public ObservableList<Lesion> selectAllByInclusion(int idInclusion) {
        ObservableList<Lesion> lesions = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("SELECT * FROM lesion WHERE ID_INCLUSION = ? ORDER BY ID");
            preparedStatement.setInt(1, idInclusion);
            resultSet = preparedStatement.executeQuery();
            this.addToObservableList(lesions, resultSet);

            System.out.println("SELECT * FROM lesion ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lesions;
    }

    private void addToObservableList(ObservableList<Lesion> lesions, ResultSet resultSet) {
        try {
            while (resultSet.next())
                lesions.add(this.addToLesion(resultSet));
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Lesion addToLesion(ResultSet resultSet) throws SQLException {
        Lesion lesion = new Lesion();

        lesion.setId(resultSet.getInt("ID"));
        lesion.setIdInclusion(resultSet.getInt("ID_INCLUSION"));
        lesion.setPhotoSur(resultSet.getString("PHOTO_SUR"));
        lesion.setPhotoHors(resultSet.getString("PHOTO_HORS"));
        lesion.setPhotoFixe(resultSet.getString("PHOTO_FIXE"));
        lesion.setSiteAnatomique(resultSet.getString("SITE_ANATOMIQUE"));
        lesion.setDiag(resultSet.getString("DIAGNOSTIC"));
        lesion.setAutreDiag(resultSet.getString("AUTRE_DIAG"));
        lesion.setFileDiag(resultSet.getString("FILE_DIAG"));

        return lesion;
    }

    @Override
    public ObservableList<Lesion> selectAll() {
        ObservableList<Lesion> lesions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = LesionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lesion ORDER BY ID");
            this.addToObservableList(lesions, resultSet);

            System.out.println("SELECT * FROM lesion ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lesions;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object) throws SQLException {
        preparedStatement.setInt(1, ((Lesion) object).getIdInclusion());
        preparedStatement.setString(2, ((Lesion) object).getPhotoSur());
        preparedStatement.setString(3, ((Lesion) object).getPhotoHors());
        preparedStatement.setString(4, ((Lesion) object).getPhotoFixe());
        preparedStatement.setString(5, ((Lesion) object).getSiteAnatomique());

        if (((Lesion) object).getDiag() == null)
            preparedStatement.setString(6, null);
        else preparedStatement.setString(6, ((Lesion) object).getDiag().toString());

        preparedStatement.setString(7, ((Lesion) object).getAutreDiag());
        preparedStatement.setString(8, ((Lesion) object).getFileDiag());

        return preparedStatement;
    }

    @Override
    public void update(Lesion lesion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("UPDATE lesion SET " + "ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.setInt(10, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE inclusion SET ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ?, FICHIER_MOY = ? WHERE ID = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getLastId() {
        Statement statement = null;
        ResultSet resultSet = null;
        int lastId = 0;

        try {
            statement = LesionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT last_insert_id() AS lastId FROM lesion");

            if (resultSet.next())
                lastId = resultSet.getInt("lastId");
            else return -1;

            System.out.println("SELECT last_insert_id() AS lastId FROM lesion");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lastId;
    }
}
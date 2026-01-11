package ma.projet.restclient;

// Importations nécessaires pour l'activité Android
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

// Importations des composants d'interface utilisateur
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Importations des composants personnalisés
import ma.projet.restclient.adapter.CompteAdapter;
import ma.projet.restclient.entities.Compte;
import ma.projet.restclient.repository.CompteRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Importations pour la gestion des dates et des collections
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

// Importations de Retrofit pour les appels réseau
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activité principale de l'application gérant l'affichage et la manipulation des comptes
 * Implémente les interfaces pour gérer les clics sur les boutons de suppression et de mise à jour
 */
public class MainActivity extends AppCompatActivity implements CompteAdapter.OnDeleteClickListener, CompteAdapter.OnUpdateClickListener {
    // Composants d'interface utilisateur
    private RecyclerView recyclerView;  // Pour afficher la liste des comptes
    private CompteAdapter adapter;      // Adaptateur pour la RecyclerView
    private RadioGroup formatGroup;     // Groupe de boutons radio pour le format de données (JSON/XML)
    private FloatingActionButton addbtn; // Bouton pour ajouter un nouveau compte
    
    // Variables d'état
    private String currentFormat = "JSON";  // Format de données actuellement sélectionné
    
    /**
     * Méthode appelée lors de la création de l'activité
     * Initialise l'interface utilisateur et charge les données initiales
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialisation des vues et des écouteurs
        initViews();
        setupRecyclerView();
        setupFormatSelection();
        setupAddButton();
        
        // Chargement initial des données au format JSON par défaut
        loadData("JSON");
    }
    
    /**
     * Initialise les vues en les liant aux éléments du layout XML
     */
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);  // RecyclerView pour afficher la liste
        formatGroup = findViewById(R.id.formatGroup);    // Groupe de boutons radio pour le format
        addbtn = findViewById(R.id.fabAdd);              // Bouton d'ajout flottant
    }
    
    /**
     * Configure le RecyclerView avec son gestionnaire de mise en page et son adaptateur
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Utilisation d'un LinearLayoutManager
        adapter = new CompteAdapter(this, this);  // Création de l'adaptateur avec les écouteurs
        recyclerView.setAdapter(adapter);  // Attribution de l'adaptateur au RecyclerView
    }
    
    /**
     * Configure l'écouteur pour la sélection du format de données (JSON/XML)
     * Recharge les données dans le format sélectionné
     */
    private void setupFormatSelection() {
        formatGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Détermine le format sélectionné (JSON ou XML)
            String format = checkedId == R.id.radioJson ? "JSON" : "XML";
            currentFormat = format;  // Met à jour le format courant
            loadData(format);  // Recharge les données dans le nouveau format
        });
    }
    
    /**
     * Configure le bouton d'ajout pour afficher la boîte de dialogue d'ajout de compte
     */
    private void setupAddButton() {
        addbtn.setOnClickListener(v -> showAddCompteDialog());  // Affiche la boîte de dialogue au clic
    }
    
    /**
     * Affiche une boîte de dialogue pour ajouter un nouveau compte
     * Permet de saisir le solde et le type de compte
     */
    private void showAddCompteDialog() {
        // Création du constructeur de la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        
        // Création de la vue à partir du layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        
        // Récupération des vues du formulaire
        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);
        
        // Configuration de la boîte de dialogue
        builder.setView(dialogView)
            .setTitle("Ajouter un compte")
            .setPositiveButton("Ajouter", (dialog, which) -> {
                // Vérification de la saisie du solde
                String solde = etSolde.getText().toString();
                if (solde.isEmpty()) {
                    showToast("Veuillez saisir un solde");
                    return;
                }
                
                try {
                    // Détermination du type de compte sélectionné
                    String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
                        ? "COURANT" : "EPARGNE";
                    
                    // Création d'un nouveau compte avec la date courante
                    String formattedDate = getCurrentDateFormatted();
                    Compte compte = new Compte(null, Double.parseDouble(solde), type, formattedDate);
                    
                    // Appel de la méthode pour ajouter le compte
                    addCompte(compte);
                } catch (NumberFormatException e) {
                    showToast("Solde invalide");
                }
            })
            .setNegativeButton("Annuler", null);  // Bouton pour annuler sans rien faire
        
        // Création et affichage de la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Génère la date courante au format "yyyy-MM-dd"
     * @return La date formatée en chaîne de caractères
     */
    private String getCurrentDateFormatted() {
        Calendar calendar = Calendar.getInstance();  // Obtient l'instance du calendrier avec la date/heure actuelles
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  // Définit le format de date
        return formatter.format(calendar.getTime());  // Formate la date selon le pattern défini
    }
    
    /**
     * Ajoute un nouveau compte via le repository
     * @param compte Le compte à ajouter
     */
    private void addCompte(Compte compte) {
        // Création d'une instance du repository avec le format actuel (JSON/XML)
        CompteRepository compteRepository = new CompteRepository(currentFormat);
        
        // Appel asynchrone pour ajouter le compte
        compteRepository.addCompte(compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte ajouté");
                    loadData(currentFormat);  // Recharge la liste des comptes après l'ajout
                } else {
                    // Affiche le code d'erreur HTTP en cas d'échec
                    showToast("Erreur lors de l'ajout: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                // Gestion des erreurs réseau ou autres échecs
                showToast("Erreur lors de l'ajout: " + t.getMessage());
            }
        });
    }
    
    /**
     * Charge la liste des comptes depuis le serveur dans le format spécifié
     * @param format Le format de données à utiliser (JSON/XML)
     */
    private void loadData(String format) {
        // Création d'une instance du repository avec le format spécifié
        CompteRepository compteRepository = new CompteRepository(format);
        
        // Récupération asynchrone de tous les comptes
        compteRepository.getAllCompte(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mise à jour de l'interface utilisateur avec les nouvelles données
                    List<Compte> comptes = response.body();
                    runOnUiThread(() -> adapter.updateData(comptes));
                } else {
                    showToast("Erreur: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                // Gestion des erreurs de connexion
                showToast("Erreur de connexion: " + t.getMessage());
            }
        });
    }
    
    /**
     * Méthode appelée lors du clic sur le bouton de mise à jour d'un compte
     * @param compte Le compte à mettre à jour
     */
    @Override
    public void onUpdateClick(Compte compte) {
        showUpdateCompteDialog(compte);  // Affiche la boîte de dialogue de modification
    }
    
    /**
     * Affiche une boîte de dialogue pour modifier un compte existant
     * @param compte Le compte à modifier
     */
    private void showUpdateCompteDialog(Compte compte) {
        // Création du constructeur de la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        
        // Création de la vue à partir du layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);
        
        // Récupération des vues et initialisation avec les valeurs actuelles du compte
        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);
        
        // Pré-remplissage des champs avec les valeurs actuelles du compte
        etSolde.setText(String.valueOf(compte.getSolde()));
        if (compte.getType().equalsIgnoreCase("COURANT")) {
            typeGroup.check(R.id.radioCourant);  // Sélectionne le bouton radio "Courant"
        } else if (compte.getType().equalsIgnoreCase("EPARGNE")) {
            typeGroup.check(R.id.radioEpargne);  // Sélectionne le bouton radio "Épargne"
        }
        
        // Configuration de la boîte de dialogue
        builder.setView(dialogView)
            .setTitle("Modifier un compte")
            .setPositiveButton("Modifier", (dialog, which) -> {
                // Validation de la saisie du solde
                String solde = etSolde.getText().toString();
                if (solde.isEmpty()) {
                    showToast("Veuillez saisir un solde");
                    return;
                }
                
                try {
                    // Mise à jour des propriétés du compte
                    String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
                        ? "COURANT" : "EPARGNE";
                    compte.setSolde(Double.parseDouble(solde));
                    compte.setType(type);
                    
                    // Appel de la méthode de mise à jour
                    updateCompte(compte);
                } catch (NumberFormatException e) {
                    showToast("Solde invalide");
                }
            })
            .setNegativeButton("Annuler", null);  // Bouton d'annulation
        
        // Affichage de la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Met à jour un compte existant via le repository
     * @param compte Le compte à mettre à jour avec les nouvelles valeurs
     */
    private void updateCompte(Compte compte) {
        // Création d'une instance du repository avec le format actuel
        CompteRepository compteRepository = new CompteRepository(currentFormat);
        
        // Appel asynchrone pour mettre à jour le compte
        compteRepository.updateCompte(compte.getId(), compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte modifié avec succès");
                    loadData(currentFormat);  // Recharge la liste des comptes
                } else {
                    showToast("Erreur lors de la modification (Code: " + response.code() + ")");
                }
            }
            
            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                // Gestion des erreurs réseau
                showToast("Erreur réseau lors de la modification: " + t.getMessage());
            }
        });
    }
    
    /**
     * Méthode appelée lors du clic sur le bouton de suppression d'un compte
     * @param compte Le compte à supprimer
     */
    @Override
    public void onDeleteClick(Compte compte) {
        showDeleteConfirmationDialog(compte);  // Affiche la boîte de dialogue de confirmation
    }
    
    /**
     * Affiche une boîte de dialogue de confirmation avant la suppression
     * @param compte Le compte à supprimer
     */
    private void showDeleteConfirmationDialog(Compte compte) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmation de suppression")
            .setMessage("Voulez-vous vraiment supprimer ce compte ?")
            .setPositiveButton("Oui", (dialog, which) -> deleteCompte(compte))  // Confirmation : supprime le compte
            .setNegativeButton("Non", null)  // Annulation : ne fait rien
            .show();
    }
    
    /**
     * Supprime un compte via le repository
     * @param compte Le compte à supprimer
     */
    private void deleteCompte(Compte compte) {
        // Création d'une instance du repository avec le format actuel
        CompteRepository compteRepository = new CompteRepository(currentFormat);
        
        // Appel asynchrone pour supprimer le compte
        compteRepository.deleteCompte(compte.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Compte supprimé avec succès");
                    loadData(currentFormat);  // Recharge la liste des comptes
                } else {
                    showToast("Erreur lors de la suppression (Code: " + response.code() + ")");
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Gestion des erreurs réseau
                showToast("Erreur réseau lors de la suppression: " + t.getMessage());
            }
        });
    }
    
    /**
     * Affiche un message toast à l'écran
     * @param message Le message à afficher
     */
    private void showToast(String message) {
        // S'assure que le Toast s'affiche sur le thread UI
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());
    }
}


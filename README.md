# Practice-Sessions



<b>Step 2:</b>

Features:
* RecyclerView with multipe Item types
* Sorting by SessionDate in the SessionRepository class
* Error handling via Live Data


<b>Step 1:</b>

Features:
* MVVM Architecture with scaleable Repository (e.g. to add a local data source), Live Data and AndroidViewModel implementations
* Basis for a flexible layout by using Fragments
* Data Binding
* Retrofit is used for remote requests

Possible Improvements:
* Error handling. Propagation from Repository by providing a Live Data interface for Throwables
* Basic network check is implemented but does not actually ping an external resource to check the connection quality
* Localisation! Time and Date format does not consider the phones locale. A SimpleDateFormat is used instead
* Responsive design

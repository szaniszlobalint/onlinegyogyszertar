# onlinegyogyszertar
Mobilalkalmazás fejlesztés

Egy kis segítség a pontozáshoz:

Fordítási hiba nincs: kész, 1 pont
Futtatási hiba nincs: kész, 1 pont
Firebase autentikáció meg van valósítva: Be lehet jelentkezni és regisztrálni: kész, 4 pont (MainActivity, 210-es sor, RegistrationActivity, 64-es sor)
Adatmodell definiálása: kész, 2 pont (Product.java)
Legalább 3 különböző activity használata: kész, 3 pont (MainActivity, RegistrationActivity, CartActivity)
Beviteli mezők típusa megfelelő: kész, 3 pont (loginlayout.xml, 39-es és 60-as sor, activity_registration.xml 41-es és 62-es sor)
ConstraintLayout és még egy másik Layout használata: kész, 1 pont (product_layout.xml-ben LinearLayout)
Reszponzív: jól jelennek meg, 3 pont (2 dimens.xml fájlba kiszervezve a méretek értékei, az egyik direkt kisebb telefonokra méretezve)
Legalább 2 különböző animáció: kész, 2 pont(cart_animation.xml felhasználva MainActivity.java-ban az 55-ös sorban, és login_icon_animation.xml, MainActivity.java-ban a 60-as sorban) 
Intentek használata, minden Activity elérhető: kész, 2 pont(például MainActivity.java 164-es sor)
Legalább 1 lifecycle hook: van, 2 pont(MainActivity.java-ban onStart, 119-es sor, ha már be vagyunk jelentkezve, értesít róla)
android permission: van, 1 pont(AndroidManifest.xml-ben, 4-es sor)
notification: van, 2 pont (CartActivity, 65-ös sor, ha vásárlunk akkor küld egy értesítést)
CRUD műveletek: csak create és read van(a regisztráció meg bejelentkezés firebase-l, firebase asynctaskot használ,) - 3 pont?
komplex firestore lekérdezés: orderby (MainActivity-ben, 71-es sor) - 2 pont

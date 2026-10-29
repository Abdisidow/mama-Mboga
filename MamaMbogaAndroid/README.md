# Mama Mboga — Android prototype

A Kotlin and XML starter app for a neighborhood fresh-produce shop. It includes a small sample catalogue, category filters, a basket, delivery or pickup selection, and social links. Placing an order prepares a message in WhatsApp so the seller and customer can confirm availability, delivery details, and payment.

## Business model

- **Customers:** nearby households and small food businesses that want convenient access to fresh, fairly priced produce.
- **Value:** local market sourcing, daily availability, transparent unit prices, and direct personal service through WhatsApp.
- **Revenue:** margin on produce; optional delivery fee for nearby orders; bundles or weekly produce boxes as repeat-purchase offers.
- **Operations:** Mama Mboga updates stock and prices daily, confirms orders, assembles produce, and arranges pickup or local delivery. Start with WhatsApp confirmation and manual M-PESA/cash settlement; add stock management and payment integration only after the workflow is validated.
- **Customer acquisition:** existing Instagram, Facebook, X/Twitter, and WhatsApp accounts, with the app acting as a simple catalogue and order entry point.
- **Key measures:** order conversion, average order value, repeat purchase rate, fulfilment time, and spoilage.

## Prototype scope

The app uses sample products and prices in Kenyan shillings. The social profile URLs and WhatsApp phone number are examples and must be replaced with Mama Mboga's real public account links and Kenyan phone number in `MainActivity.kt`. Prices, stock, delivery zones/fees, and opening hours are also sample values. The app does not connect to a backend, live inventory, or M-PESA API. Orders are handed off to WhatsApp for confirmation.

## Open and run

Open this folder in Android Studio and allow Gradle sync. Run the `app` configuration on an Android device or emulator (minimum Android 6.0 / API 23). The project targets SDK 35.

## Next steps for a production release

1. Confirm her brand name, location, delivery boundary, hours, delivery fee, actual social links, and WhatsApp number.
2. Replace sample catalogue data with a seller-editable catalogue and stock/price updates.
3. Add order persistence, customer contact/address, order status, and a seller order view.
4. Integrate M-PESA through a server-side payment provider and verify callbacks before marking orders paid.
5. Add privacy notice, terms, accessibility review, and device testing before release.

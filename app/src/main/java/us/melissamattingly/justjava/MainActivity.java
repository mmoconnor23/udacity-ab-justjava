package us.melissamattingly.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.max_quantity), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.min_quantity), Toast.LENGTH_SHORT).show();
            return;
        }

        quantity--;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCream = findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolate = findViewById(R.id.chocolate_checkbox);
        boolean wantsWhippedCream = whippedCream.isChecked();
        boolean wantsChocolate = chocolate.isChecked();
        EditText name = findViewById(R.id.person_name_text);

        sendAndCreateOrderSummary(
            calculatePrice(wantsWhippedCream, wantsChocolate),
            wantsWhippedCream,
            wantsChocolate,
            name.getText().toString()
        );
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * Calculates the price of the order.
     *
     * @param wantsWhippedCream If they want whipped cream
     * @param wantsChocolate If they want chocolate
     *
     * @return The price of the current quantity of cups of coffee
     */
    private int calculatePrice(boolean wantsWhippedCream, boolean wantsChocolate) {
        int basePrice = 5;

        if (wantsWhippedCream) {
            basePrice += 1;
        }

        if (wantsChocolate) {
            basePrice += 2;
        }
        return quantity * basePrice;
    }

    /**
     * Creates a summary of the client's order and opens it in an email.
     *
     * @param price The price of the order
     * @param wantsWhippedCream If they want whipped cream
     * @param wantsChocolate If they want chocolate
     * @param name The name that was input
     */
    private void sendAndCreateOrderSummary(int price,
                                      boolean wantsWhippedCream,
                                      boolean wantsChocolate,
                                      String name) {
        String emailContent = getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.order_summary_whipped_cream, wantsWhippedCream) +
                "\n" + getString(R.string.order_summary_chocolate, wantsChocolate) +
                "\n" + getString(R.string.order_summary_quantity, quantity) +
                "\n" + getString(R.string.order_summary_price, price) +
                "\n" + getString(R.string.thank_you);

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:")); // only email apps should handle this
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        email.putExtra(Intent.EXTRA_TEXT, emailContent);
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        }
    }
}

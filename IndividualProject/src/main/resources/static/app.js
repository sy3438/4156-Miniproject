const API_KEY = "540ba996-2ae9-4f72-8b4f-014b463bc4d7";

document.getElementById("compareButton").addEventListener("click", compareTaxes);

async function compareTaxes() {
    const price = Number(document.getElementById("price").value);
    const category = document.getElementById("category").value;
    const state1 = document.getElementById("state1").value;
    const state2 = document.getElementById("state2").value;

    const results = document.getElementById("results");
    results.innerHTML = "Loading...";

    try {
        const firstResult = await getTaxQuote(state1, price, category);
        const secondResult = await getTaxQuote(state2, price, category);

        results.innerHTML = `
            <div class="result">
                <h2>${state1}</h2>
                <p>Tax Rate: ${(firstResult.taxRate * 100).toFixed(2)}%</p>
                <p>Tax: $${firstResult.taxAmount.toFixed(2)}</p>
                <p>Total: $${firstResult.total.toFixed(2)}</p>
            </div>

            <div class="result">
                <h2>${state2}</h2>
                <p>Tax Rate: ${(secondResult.taxRate * 100).toFixed(2)}%</p>
                <p>Tax: $${secondResult.taxAmount.toFixed(2)}</p>
                <p>Total: $${secondResult.total.toFixed(2)}</p>
            </div>
        `;
    } catch (error) {
        results.innerHTML = "<p>Could not calculate taxes.</p>";
    }
}

async function getTaxQuote(state, price, category) {
    const response = await fetch("/v1/tax/quote", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-API-Key": API_KEY
        },
        body: JSON.stringify({
            state: state,
            price: price,
            category: category
        })
    });

    if (!response.ok) {
        throw new Error("Tax API request failed");
    }

    return response.json();
}
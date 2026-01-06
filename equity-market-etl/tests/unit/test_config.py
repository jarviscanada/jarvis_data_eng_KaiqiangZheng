from src.utils.config import Paths

def test_default_paths_exist_format():
    p = Paths()

    # If we do not write error messages (the second argument), pytest will just say "AssertionError" and provide the statement itself.
    # Ensure RAW_PRICES_DIR starts with "/opt/data" or "/"
    assert p.RAW_PRICES_DIR.startswith("/opt/data") or p.RAW_PRICES_DIR.startswith("/"), \
        f"RAW_PRICES_DIR should start with '/opt/data' or '/' but got {p.RAW_PRICES_DIR}"

    # Ensure RAW_COMPANIES_CSV ends with ".csv"
    assert p.RAW_COMPANIES_CSV.endswith(".csv"), \
        f"RAW_COMPANIES_CSV should end with '.csv' but got {p.RAW_COMPANIES_CSV}"
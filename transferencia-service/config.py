from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    DATABASE_URL: str
    JWT_ACCESS_KEY: str | None = None
    # Accept legacy SECRET_KEY env var as well
    SECRET_KEY: str | None = None
    ALGORITHM: str = "HS256"

    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8")


settings = Settings()

# If user provided SECRET_KEY in env (legacy), use it for JWT_ACCESS_KEY
if not settings.JWT_ACCESS_KEY and settings.SECRET_KEY:
    settings.JWT_ACCESS_KEY = settings.SECRET_KEY

# Backwards-compatibility property: allow reading settings.SECRET_KEY
def _secret_key_getter(self):
    return getattr(self, "JWT_ACCESS_KEY")

Settings.SECRET_KEY = property(_secret_key_getter)

# 1. Start with a modern, official PHP image.
FROM php:8.3-cli-alpine

# 2. Install necessary system dependencies and Composer.
RUN apk add --no-cache git \
    && curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

# 3. INSTALL THE MISSING PCNTL EXTENSION.
# This is the line that fixes the fatal error.
RUN docker-php-ext-install pcntl

# 4. Set a working directory inside the container.
WORKDIR /app

# 5. Use Composer to install Phpactor globally.
RUN composer global require phpactor/phpactor

# 6. Add the global Composer bin directory to the system's PATH.
ENV PATH="/root/.composer/vendor/bin:${PATH}"

# 7. Expose the default Phpactor port.
EXPOSE 8737

# 8. Define the default command to run when the container starts.
CMD ["phpactor", "language-server", "--address=0.0.0.0:8737"]
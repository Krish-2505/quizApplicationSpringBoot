// circular-progress-bar.js

function CircularProgressBar(containerId, initialValue) {
  const maxValue = 100;
  const container = document.getElementById(containerId);
  const svgCircle = container.querySelector(".foreground-circle svg circle");
  const numberInsideCircle = container.querySelector("#number-inside-circle");
  const remainingText = container.querySelector("#remaining-text");

  const svgStrokeDashArray = parseInt(
    window.getComputedStyle(svgCircle).getPropertyValue("stroke-dasharray").replace("px", "")
  );

  let previousStrokeDashOffset = svgStrokeDashArray;
  let previousValue = 0;
  const animationDuration = 1000;

  function animateCircle(value, labelText) {
    const offsetValue = Math.floor(((maxValue - value) * svgStrokeDashArray) / maxValue);

    svgCircle.animate(
      [
        { strokeDashoffset: previousStrokeDashOffset },
        { strokeDashoffset: offsetValue },
      ],
      { duration: animationDuration }
    );

    svgCircle.style.strokeDashoffset = offsetValue;

    if (value !== previousValue) {
      const speed = animationDuration / Math.abs(value - previousValue);
      let counter = previousValue;

      const intervalId = setInterval(() => {
        if (counter === value || counter === -1) {
          clearInterval(intervalId);
          previousStrokeDashOffset = offsetValue;
          previousValue = value;
        } else {
          if (value > previousValue) {
            counter += 1;
          } else {
            counter -= 1;
          }

          numberInsideCircle.innerHTML = counter;
          remainingText.innerHTML = labelText;
        }
      }, speed);
    }
  }

  animateCircle(initialValue, remainingText.innerHTML);

  return {
    animateCircle,
  };
}

// Usage example:
const circularProgressBar = CircularProgressBar('progress-container', 12);

// To animate with a different value and label:
// circularProgressBar.animateCircle(newValue, newLabelText);

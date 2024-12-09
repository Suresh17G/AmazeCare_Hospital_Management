import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Pagination, Autoplay } from 'swiper/modules'; 
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import './Service.css';

const Services = () => {
  const services = [
    { title: 'Orthopedic', image: '/images/assets/Ortho.png' },
    { title: 'Neurosciences', image: '/images/assets/nuerov2.jpg' },
    { title: 'Cardiology', image: '/images/assets/Cardiology.png' },
    { title: 'Orthopedic', image: '/images/assets/Ortho.png' },
    { title: 'Neurosciences', image: '/images/assets/nuerov2.jpg' },
    { title: 'Cardiology', image: '/images/assets/Cardiology.png' },
    { title: 'Dental Care', image: '/images/assets/dental_care.png' },
  ];

  return (
    <section className="services">
      <h2>Our Services</h2>
      <Swiper
        modules={[Navigation, Pagination, Autoplay]} 
        navigation
        pagination={{ clickable: true }}
        autoplay={{ delay: 3000 }}
        loop
        spaceBetween={20}
        slidesPerView={1}
        breakpoints={{
          640: { slidesPerView: 1 },
          768: { slidesPerView: 2 },
          1024: { slidesPerView: 3 },
        }}
      >
        {services.map((service, index) => (
          <SwiperSlide key={index}>
            <div className="service-card">
              <img src={service.image} alt={service.title} />
              <h3>{service.title}</h3>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </section>
  );
};

export default Services;

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MenuPlatsComponent} from './menu-plats.component';

describe('MenuPlatsComponent', () => {
  let component: MenuPlatsComponent;
  let fixture: ComponentFixture<MenuPlatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuPlatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuPlatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

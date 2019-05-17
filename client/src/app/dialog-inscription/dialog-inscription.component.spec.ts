import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogInscriptionComponent } from './dialog-inscription.component';

describe('DialogInscriptionComponent', () => {
  let component: DialogInscriptionComponent;
  let fixture: ComponentFixture<DialogInscriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogInscriptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogInscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
